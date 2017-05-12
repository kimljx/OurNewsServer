/**
 * Created by ternence on 2017/3/9.
 */
define(["jquery", "backbone", "text!templates/HeaderView.html"],
    function ($, Backbone, template) {
        var HeaderView = Backbone.View.extend({
            initialize: function () {
                this.render();
            },
            events: {
                "click .toggle-btn": "toggleBtnClick",
                "change .search-content input": "searchContentChange",
                "click #exit": "exit",
            },
            render: function () {
                this.template = _.template(template);
                this.$el.html(this.template(this));
                return this;
            },
            toggleBtnClick: function () {
                var body = jQuery('body');
                var bodyposition = body.css('position');

                if (bodyposition != 'relative') {

                    if (!body.hasClass('sidebar-collapsed')) {
                        body.addClass('sidebar-collapsed');
                        jQuery('.side-navigation ul').attr('style', '');

                    } else {
                        body.removeClass('sidebar-collapsed chat-view');
                        jQuery('.side-navigation li.active ul').css({display: 'block'});

                    }
                } else {

                    if (body.hasClass('sidebar-open'))
                        body.removeClass('sidebar-open');
                    else
                        body.addClass('sidebar-open');

                    //adjustMainContentHeight();
                }

                var owl = $("#news-feed").data("owlCarousel");
                if (owl) {
                    owl.reinit();
                }
            },
            searchContentChange: function () {
                de
                location.replace("#homepage");
            },
            getCookie: function (name) {
                var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
                if (arr = document.cookie.match(reg))
                    return unescape(arr[2]);
                else
                    return null;
            },
            clearCookie: function (name) {
                var Days = 5;
                var exp = new Date();
                exp.setTime(exp.getTime() + Days*1000);
                var cval = this.getCookie(name);
                if (cval != null)
                    document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
            },
            exit: function () {
                this.clearCookie("id");
                this.clearCookie("token");
                location.replace(AppConfig.loginURL);
            }
        })


        return HeaderView;
    })