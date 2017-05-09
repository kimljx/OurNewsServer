/**
 * Created by ternence on 2017/3/19.
 */
define(["jquery", "backbone", "models/newsDetailModel"],

    function ($, Backbone, NewsDetailModel) {
        // Creates a new Backbone Collection class object
        var NewsDetailCollection = Backbone.Collection.extend({

            // Tells the Backbone Collection that all of it's models will be of type Model (listed up top as a dependency)
            model: NewsDetailModel,
            initialize: function () {
                var self=this;
                // this.fetchNewsDetailDataFromServer(newsIdVal,newsAbstractVal,newsCreateTimeVal)
            },
            getNewsDetailData: function () {
                var models=[]
                _.each(this.models,function (item) {
                    if(item.id){
                        models=item;
                    }
                })
                return models;
            },
            fetchNewsDetailDataFromServer: function (newsIdVal,newsAbstractVal,newsCreateTimeVal) {
                var self = this;
                self.models=[];
                return APIService.callAPI(AppConfig.apiURL + 'getNewContentForWeb', 'POST', {
                    nid: Number(newsIdVal),
                    uid: Number(document.cookie.split(";")[0].split("=")[1])
                }).then(function (resp) {
                    var tempList=[];
                    var isTextStart=false;
                    var newsDetailModel = new NewsDetailModel();
                    newsDetailModel.id = resp.id;
                    newsDetailModel.content = resp.content;
                    newsDetailModel.manager={
                        id:resp.manager.id,
                        nick_name:resp.manager.nick_name,
                        sex:resp.manager.sex,
                        sign:resp.manager.sign,
                        birthday:resp.manager.birthday,
                        photo:resp.manager.photo
                    };
                    // newsDetailModel.manager.push({
                    //     id:resp.manager.id,
                    //     nick_name:resp.manager.nick_name,
                    //     sex:resp.manager.sex,
                    //     sign:resp.manager.sign,
                    //     birthday:resp.manager.birthday,
                    //     photo:resp.manager.photo
                    // })
                    var regex = new RegExp("<json>", 'g');
                    var regexName = new RegExp("{\"name\":", 'g');
                    var result = newsDetailModel.content.match(regex);
                    var count = !result ? 0 : result.length;
                    for(var i=0;;i++){
                        if(i==0){
                            if(newsDetailModel.content.split("<json>")[0]){
                                tempList.push(newsDetailModel.content.split("<json>")[0]);
                                isTextStart=true;
                            }else if(newsDetailModel.content.split("<json>")[1].split("</json>")[0]){
                                tempList.push(newsDetailModel.content.split("<json>")[1].split("</json>")[0]);
                                isTextStart=false;
                            }else{
                                break;
                            }
                        }else{
                            if(isTextStart){
                                if(newsDetailModel.content.split("<json>")[i]/*.split("</json>")[0]*/){
                                    tempList.push(newsDetailModel.content.split("<json>")[i].split("</json>")[0]);
                                    if(newsDetailModel.content.split("<json>")[i].split("</json>")[1]){
                                        tempList.push(newsDetailModel.content.split("<json>")[i].split("</json>")[1])
                                    }
                                    // else if(newsDetailModel.content.split("<json>")[i+1]){
                                    //     continue;
                                    // }
                                    else {
                                        break;
                                    }
                                }else {
                                    break;
                                }
                            }else {
                                if(newsDetailModel.content.split("<json>")[i]/*.split("</json>")[1]*/){
                                    tempList.push(newsDetailModel.content.split("<json>")[i].split("</json>")[1]);
                                    if(newsDetailModel.content.split("<json>")[i].split("</json>")[0]){
                                        tempList.push(newsDetailModel.content.split("<json>")[i].split("</json>")[0]);
                                    }else {
                                        break;
                                    }
                                }else {
                                    break;
                                }
                            }

                        }
                    }
                    for(var i=0;i<tempList.length;i++){
                        if(isTextStart){
                            if(!tempList[i].match(regexName)){
                                tempList[i]="<p>"+tempList[i]+"</p>";
                            }else {
                                tempList[i]="<img src="+AppConfig.apiPicURL+"?name="+tempList[i].split(":\"")[1].split("\",")[0]+">";
                            }
                        }else {
                            if(tempList[i].match(regexName)){
                                tempList[i]="<img src="+AppConfig.apiPicURL+"?name="+tempList[i].split(":\"")[1].split("\",")[0]+">";
                            }else {
                                tempList[i]="<p>"+tempList[i]+"</p>";
                            }
                        }
                    }
                    newsDetailModel.contentList=tempList.join("");
                    newsDetailModel.is_collected = resp.is_collected;
                    newsDetailModel.comment_num = resp.comment_num;
                    newsDetailModel.history_num = resp.history_num;
                    newsDetailModel.collection_num = resp.collection_num;
                    newsDetailModel.newsAbstractVal = newsAbstractVal;
                    newsDetailModel.newsCreateTimeVal = newsCreateTimeVal;
                    self.models.push(newsDetailModel);
                    // self.trigger('newsChange');
                    self.trigger('changeNewsDetail');
                })

            },
            collectNewToServer:function (nid,type,newsIdVal,newsAbstractVal,newsCreateTimeVal) {
                var self=this;
                return APIService.callAPI(AppConfig.apiURL + 'collectNew', 'POST', {
                    nid:Number(nid),
                    uid:Number(document.cookie.split(";")[0].split("=")[1]),
                    token:document.cookie.split(";")[1].split("=")[1],
                    type:type,
                }).then(function (resp) {
                    self.fetchNewsDetailDataFromServer(newsIdVal,newsAbstractVal,newsCreateTimeVal);
                })
            },
            // getCommentDataFromServer:function (newsIdVal,page,size,sort) {
            //     return APIService.callAPI(AppConfig.apiURL+'getComment','POST',{
            //         nid:Number(newsIdVal),
            //         page:page,
            //         size:size,
            //         sort:sort,
            //         uid:Number(document.cookie.split("=")[1].split(",")[0])
            //     }).then(function (resp) {
            //         debugger;
            //         resp.comments
            //     })
            // }
        });

        // Returns the Model class
        return NewsDetailCollection;

    }
);