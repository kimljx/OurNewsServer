/**
 * Created by ternence on 2017/3/23.
 */
/**
 * Created by ternence on 2017/3/15.
 */
define(["jquery", "backbone", "models/userInfoUpdateModalModel"],

    function ($, Backbone, UserInfoUpdateModalModel) {
        // Creates a new Backbone Collection class object
        var UserInfoUpdateModalCollection = Backbone.Collection.extend({

            // Tells the Backbone Collection that all of it's models will be of type Model (listed up top as a dependency)
            model: UserInfoUpdateModalModel,
            initialize: function () {
                // this.fetchUserInfoDataFromServer()
            },
            getUserInfoData: function () {
                return this.models;
            },
            // fetchUserInfoDataFromServer: function () {
            //     var self = this;
            //     return APIService.callAPI(AppConfig.apiURL + 'checkLogin', 'POST', {
            //         id: document.cookie.split("=")[1].split(",")[0],
            //         token: document.cookie.split("=")[2]
            //     }).then(function (resp) {
            //         debugger;
            //         var birthday='';
            //         var userInfoUpdateModalModel=new UserInfoUpdateModalModel();
            //         userInfoUpdateModalModel.id = resp.id;
            //         userInfoUpdateModalModel.login_name = resp.login_name;
            //         if(!resp.nick_name){
            //             resp.nick_name=resp.login_name;
            //         }
            //         userInfoUpdateModalModel.nick_name = resp.nick_name;
            //         userInfoUpdateModalModel.sex = resp.sex;
            //         userInfoUpdateModalModel.sign = resp.sign;
            //         if(!resp.birthday){
            //             birthday="";
            //         }else{
            //             birthday += resp.birthday.toString().substring(0,4)+"年";
            //             birthday += resp.birthday.toString().substring(4,6)+"月";
            //             birthday += resp.birthday.toString().substring(6,8)+"日";
            //         }
            //         userInfoUpdateModalModel.birthday=birthday;
            //         userInfoUpdateModalModel.photo = resp.photo;
            //         self.models=userInfoUpdateModalModel;
            //         // self.trigger('newsChange');
            //         self.trigger('change');
            //     })
            //
            // },
            sendUserInfoUpdateDataFromServer: function (data) {
                var self = this;
                return APIService.callAPI(AppConfig.apiURL+'changeInfo', 'POST', data).then(function (resp) {
                    _.each(resp.news, function (item) {

                    })
                })

            }

        });

        // Returns the Model class
        return UserInfoUpdateModalCollection;

    }
);