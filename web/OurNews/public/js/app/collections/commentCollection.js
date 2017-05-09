/**
 * Created by ternence on 2017/3/21.
 */
define(["jquery", "backbone", "models/commentModel"],

    function ($, Backbone, CommentModel) {
        // Creates a new Backbone Collection class object
        var CommentCollection = Backbone.Collection.extend({

            // Tells the Backbone Collection that all of it's models will be of type Model (listed up top as a dependency)
            model: CommentModel,
            initialize: function () {
                // this.fetchAllHomeNewsDataFromServer();
            },
            // getHomeNewsData:function(){
            //     return this.models;
            // },
            getCommentData:function () {
                return this.modelsComment;
            },
            getCommentDataFromServer:function (newsIdVal,page,size,sort) {
                var self=this;
                return APIService.callAPI(AppConfig.apiURL+'getComment','POST',{
                    nid:Number(newsIdVal),
                    page:page,
                    size:size,
                    sort:sort,
                    uid:Number(document.cookie.split(";")[0].split("=")[1])
                }).then(function (resp) {
                    var commentModel=new CommentModel();
                    commentModel.attributes.comments=[];
                    _.each(resp.comments,function (item) {
                        var comment_children=[];
                        _.each(item.comment_children,function (comment_childrenItem) {
                            comment_children.push({
                                id:comment_childrenItem.id,
                                content:comment_childrenItem.content,
                                create_time:comment_childrenItem.create_time,
                                user:{
                                    id:comment_childrenItem.user.id,
                                    nick_name:comment_childrenItem.user.nick_name,
                                    sex:comment_childrenItem.user.sex,
                                    sign:comment_childrenItem.user.sign,
                                    birthday:comment_childrenItem.user.birthday,
                                    photo:comment_childrenItem.user.photo,
                                }
                            })
                        })
                        commentModel.attributes.comments.push({
                            id:item.id,
                            content:item.content,
                            create_time:item.create_time,
                            user:{
                                id:item.id,
                                nick_name:item.nick_name,
                                sex:item.sex,
                                sign:item.sign,
                                birthday:item.birthday,
                                photo:item.photo
                            },
                            like_num:item.like_num,
                            is_like:item.is_like,
                            comment_children:comment_children,
                            child_num:item.child_num

                        })
                    });
                    self.modelsComment=commentModel;
                    self.trigger('changeComment');
                })
            },
            sendCommentDataToServer:function (nid,content,timestamp) {
                var self=this;
                return APIService.callAPI(AppConfig.apiURL + 'sendComment', 'POST', {
                    uid:Number(document.cookie.split(";")[0].split("=")[1]),
                    nid:Number(nid),
                    content:content,
                    time:timestamp,
                    token:document.cookie.split(";")[1].split("=")[1],
                    key:$.md5("a0f33a00f12b4a8eb6f626f03a1f140a"+document.cookie.split(";")[1].split("=")[1]+timestamp),
                }).then(function (resp) {
                    self.getCommentDataFromServer(newsIdVal,1,5,1);
                })
            }
            ,
            sendLickCommentToServer:function (cid,type,newsIdVal) {
                var self=this;
                return APIService.callAPI(AppConfig.apiURL + 'lickComment', 'POST', {
                    cid:Number(cid),
                    uid:Number(document.cookie.split(";")[0].split("=")[1]),
                    token:document.cookie.split(";")[1].split("=")[1],
                    type:type,
                }).then(function (resp) {
                    self.getCommentDataFromServer(newsIdVal,1,5,1);
                })
            }

        });

        // Returns the Model class
        return CommentCollection;

    }
);