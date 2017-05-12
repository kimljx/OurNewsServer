/**
 * Created by ternence on 2017/3/23.
 */
define(["jquery", "backbone", "collections/userInfoUpdateModalCollection", "text!templates/UserInfoUpdateModalView.html"],
    function ($, Backbone, UserInfoUpdateModalCollection, template) {
        var UserInfoUpdateModalView = Backbone.View.extend({
            // The DOM Element associated with this view
            // View constructor
            initialize: function (userInfo) {
                // !options && (options = {title: '...'});
                var $this=this;
                this.model=new UserInfoUpdateModalCollection();
                this.userInfoFormat(userInfo);
                this.template=_.template(template);
                this.setElement(this.template());
                $(this.el).on('hidden.bs.modal', function () {
                    $this.remove();
                    window.history.back();
                });
                this.GetLength = function(str) {
                    return str.replace(/[\u0391-\uFFE5]/g,"aa").length;  //先把中文替换成两个字节的英文，在计算长度
                };
            },
            events: {
                "click #profile-submit": "userInfoUpdateBtn",
            },
            render: function () {

                // this.userInfoData
                // this.userInfoData.password="_.OurNews@#$*&";
                // $('body').append($(this.el).modal('show'));
                // this.$el.append(this.template(this));
                // this.template = _.template(template);
                // this.setElement(this.template());


                // this.template = _.template(template);
                // this.$el.append(this.template(this));
                // this.$el.modal('show');


                $('body').append( $(this.el).modal('show') );
                // $('.rlf-radio-group input:checked').removeAttr("checked");
                $(".rlf-radio-group input[value="+this.sex+"]").prop("checked","checked");
                // $(this.el).on('hidden.bs.modal', function () {
                //     debugger;
                //     $this.remove();
                //     window.history.back();
                // });
                return this;
            },
            userInfoUpdateBtn: function () {
                $this=this;
                var birthday='';
                var password='';
                var nickname=$('input[name=nickname]').val();
                if(!$('input[name=password]').val()=="_.OurNews@#$*&"){
                    password=$('input[name=password]').val();
                }
                var birthdayTemp=$('input[name=birthday]').val();
                if(this.GetLength(birthdayTemp.split('y')[0])){
                    birthday+=birthdayTemp.split('年')[0];
                    if(this.GetLength(birthdayTemp.split('年')[1].split('月')[0])==1){
                        birthday+="0"+birthdayTemp.split('年')[1].split('月')[0];
                    }else if(this.GetLength(birthdayTemp.split('年')[1].split('月')[0])==2){
                        birthday+=birthdayTemp.split('年')[1].split('月')[0];

                    };
                    if(this.GetLength(birthdayTemp.split('月')[1].split('日')[0])==1){
                        birthday+="0"+birthdayTemp.split('月')[1].split('日')[0];
                    }else if(this.GetLength(birthdayTemp.split('月')[1].split('日')[0])==2){
                        birthday+=birthdayTemp.split('月')[1].split('日')[0];

                    };
                }

                var sex=$('input[name=sex]:checked').val();
                var sign=$('textarea[name=aboutme]').val();
                var data={
                    id: Number(document.cookie.split(";")[0].split("=")[1]),
                    token: document.cookie.split(";")[1].split("=")[1],
                    password:password,
                    nick_name:nickname,
                    sex:Number(sex),
                    sign:sign,
                    birthday:Number(birthday),
                }
                this.model.sendUserInfoUpdateDataFromServer(data).then(function () {
                    $('div.modal-backdrop').remove();
                    $this.remove();
                    location.replace("#personalInformation");
                })
            },
            userInfoFormat:function (userInfo) {
                var birthday = '';
                this.password="_OurNews@#$*&";
                this.id = userInfo.model.get("id");
                this.login_name = userInfo.model.get("login_name");
                if (!userInfo.model.get("nick_name")) {
                    userInfo.model.set("nick_name" ,userInfo.model.get("login_name"));
                }
                this.nick_name = userInfo.model.get("nick_name");
                this.sex = userInfo.model.get("sex");
                this.sign = userInfo.model.get("sign");
                if (!userInfo.model.get("birthday")) {
                    birthday = "";
                } else {
                    birthday += userInfo.model.get("birthday").toString().substring(0, 4) + "年";
                    birthday += userInfo.model.get("birthday").toString().substring(4, 6) + "月";
                    birthday += userInfo.model.get("birthday").toString().substring(6, 8) + "日";
                }
                this.birthday = birthday;
                // this.userInfoData.photo = resp.photo;
            }




        })

        // Returns the View class
        return UserInfoUpdateModalView;

    }
);