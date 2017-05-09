/**
 * Created by ternence on 2017/3/22.
 */
define(["jquery", "backbone", "models/personalInformationModel"],

    function ($, Backbone, PersonalInformationModel) {
        // Creates a new Backbone Collection class object
        var PersonalInformationCollection = Backbone.Collection.extend({

            // Tells the Backbone Collection that all of it's models will be of type Model (listed up top as a dependency)
            model: PersonalInformationModel,
            initialize: function () {
                this.fetchUserInfoDataFromServer();
            },
            getUserInfoData: function () {
                return this.models;
            },
            fetchUserInfoDataFromServer: function () {
                var self = this;
                return APIService.callAPI(AppConfig.apiURL + 'checkLogin', 'POST', {
                    id: document.cookie.split(";")[0].split("=")[1],
                    token: document.cookie.split(";")[1].split("=")[1]
                }).then(function (resp) {
                    var birthday='';
                    var personalInformationModel=new PersonalInformationModel();
                    personalInformationModel.id = resp.id;
                    personalInformationModel.login_name = resp.login_name;
                    if(!resp.nick_name){
                        resp.nick_name=resp.login_name;
                    }
                    personalInformationModel.nick_name = resp.nick_name;
                    if(!resp.sex){
                        personalInformationModel.sex = "未设置";
                    }else if(resp.sex==1){
                        personalInformationModel.sex = "男士";
                    }else{
                        personalInformationModel.sex = "女士";
                    }
                    if(!resp.sign){
                        resp.sign="未设置"
                    }
                    personalInformationModel.sign = resp.sign;
                    if(!resp.birthday){
                        birthday="未设置";
                    }else{
                        birthday += resp.birthday.toString().substring(0,4)+"年";
                        birthday += resp.birthday.toString().substring(4,6)+"月";
                        birthday += resp.birthday.toString().substring(6,8)+"日";
                    }
                    personalInformationModel.birthday=birthday;
                    personalInformationModel.photo = resp.photo;
                    self.models=personalInformationModel;
                    // self.trigger('newsChange');
                    self.trigger('change');
                })

            }

        });

        // Returns the Model class
        return PersonalInformationCollection;

    }
);