// DesktopRouter.js
// ----------------
define([
        "jquery",
        "backbone",
        "models/userModel",
        "models/homeNewsModel",
        "models/personalHomePageModel",
        "models/headlinesNewsModel",
        "models/newsDetailModel",
        "models/commentModel",
        "models/personalInformationModel",
        "models/userInfoUpdateModalModel",
        "views/headerView",
        "views/homeNewsView",
        "views/personalHomePageView",
        "views/personalHomeBrowsingHistoryAndCollectionView",
        // "views/personalHomeBrowsingHistoryListView",
        "views/headlinesNewsView",
        "views/newsDetailView",
        "views/personalInformationView",
        "views/userInfoUpdateModalView",
        "views/userPhotoUpdateModalView",
        "collections/homeNewsCollection",
        "collections/headlinesNewsCollection",
        "collections/newsDetailCollection",
        "collections/commentCollection",
        "collections/personalInformationCollection",
        "collections/userInfoUpdateModalCollection",
    ],

    function ($, Backbone, UserModel, HomeNewsModel, PersonalHomePageModel, HeadlinesNewsModel,
              NewsDetailModel, CommentModel,PersonalInformationModel,UserInfoUpdateModalModel, HeaderView,
              HomeNewsView, PersonalHomePageView, PersonalHomeBrowsingHistoryAndCollectionView /*PersonalHomeBrowsingHistoryListView*/, HeadlinesNewsView,
              NewsDetailView,PersonalInformationView,UserInfoUpdateModalView,UserPhotoUpdateModalView) {
        var DesktopRouter = Backbone.Router.extend({

            initialize: function () {
                // Tells Backbone to start watching for hashchange events
                Backbone.history.start();
            },

            // All of your Backbone Routes (add more)
            routes: {

                // When there is no hash on the url, the home method is called
                "": "index",
                "homepage": "homepage",
                "headlinesNews": "headlinesNews",
                "newsDetail": "newsDetail",
                "personalInformation":"personalInformation",
                "userInfoUpdateModal":"userInfoUpdateModal",
                "userPhotoUpdateModal":"userPhotoUpdateModal",
            },

            index: function () {
                // Instantiates a new view which will render the header text to the page
                debugger;
                var self = this;
                window.loginUser = new UserModel();
                if(!document.cookie.split(";")[0].split("=")[1]){
                    window.location.href=AppConfig.localURL+"login.html";
                }else{
                    var promise = APIService.callAPI(AppConfig.apiURL + 'getHomeNews/checkLogin', 'POST', {
                        id: document.cookie.split(";")[0].split("=")[1],
                        token: document.cookie.split(";")[1].split("=")[1]
                    });
                    promise.then(function (resp) {
                        debugger;
                        window.loginUser.set(resp);
                        self.headerView=new HeaderView({model: window.loginUser});
                        $('.header-section').html( self.headerView.el );
                    }).then(function (resp) {
                        self.personalHomePageView=new PersonalHomePageView({model: window.loginUser});
                        $('#main-section').html( self.personalHomePageView.el );
                        // new PersonalHomeBrowsingHistoryListView();
                    })
                }



            },
            homepage: function () {
                var self = this;
                if(!window.loginUser){
                    if(!document.cookie.split(";")[0].split("=")[1]){
                        window.location.href=AppConfig.localURL+"login.html";
                    }else {
                        window.loginUser = new UserModel();
                        var promise = APIService.callAPI(AppConfig.apiURL + 'getHomeNews/checkLogin', 'POST', {
                            id: document.cookie.split(";")[0].split("=")[1],
                            token: document.cookie.split(";")[1].split("=")[1]
                        });
                        promise.then(function (resp) {
                            window.loginUser.set(resp);
                            self.headerView = new HeaderView({model: window.loginUser});
                            $('.header-section').html(self.headerView.el);
                        }).then(function (resp) {
                            self.homeNewsView = new HomeNewsView();
                            $('#main-section').html(self.homeNewsView.el);
                            // new PersonalHomeBrowsingHistoryListView();
                        })
                    }
                }else {
                    this.homeNewsView=new HomeNewsView();
                    $('#main-section').html( this.homeNewsView.el );
                }

            },
            // headlinesNews: function () {
            //     var self = this;
            //     if(!window.loginUser){
            //         window.loginUser = new UserModel();
            //         var promise = APIService.callAPI(AppConfig.apiURL + 'getHomeNews/checkLogin', 'POST', {
            //             id: document.cookie.split(";")[0].split("=")[1],
            //             token: document.cookie.split(";")[1].split("=")[1]
            //         });
            //         promise.then(function (resp) {
            //             window.loginUser.set(resp);
            //             self.headerView=new HeaderView({model: window.loginUser});
            //             $('.header-section').html( self.headerView.el );
            //         }).then(function (resp) {
            //             self.headlinesNewsView = new HeadlinesNewsView();
            //             $('#main-section').html( self.headlinesNewsView.el );
            //             // new PersonalHomeBrowsingHistoryListView();
            //         })
            //     }else {
            //         this.headlinesNewsView = new HeadlinesNewsView();
            //         $('#main-section').html( this.headlinesNewsView.el );
            //     }
            //
            // },
            newsDetail: function () {
                var self = this;
                if(!window.loginUser){
                    if(!document.cookie.split(";")[0].split("=")[1]){
                        window.location.href=AppConfig.localURL+"login.html";
                    }else {
                        window.loginUser = new UserModel();
                        var promise = APIService.callAPI(AppConfig.apiURL + 'getHomeNews/checkLogin', 'POST', {
                            id: document.cookie.split(";")[0].split("=")[1],
                            token: document.cookie.split(";")[1].split("=")[1]
                        });
                        promise.then(function (resp) {
                            window.loginUser.set(resp);
                            self.headerView = new HeaderView({model: window.loginUser});
                            $('.header-section').html(self.headerView.el);
                        }).then(function (resp) {
                            self.newsDetailView = new NewsDetailView(window.newsIdVal, window.newsAbstractVal, window.newsCreateTimeVal);
                            $('#main-section').html(self.newsDetailView.el);
                            // new PersonalHomeBrowsingHistoryListView();
                        })
                    }


                }else {
                    this.newsDetailView = new NewsDetailView(window.newsIdVal, window.newsAbstractVal, window.newsCreateTimeVal);
                    $('#main-section').html( this.newsDetailView.el );
                }

            },
            personalInformation:function () {
                var self = this;
                if(!window.loginUser){
                    if(!document.cookie.split(";")[0].split("=")[1]){
                        window.location.href=AppConfig.localURL+"login.html";
                    }else {
                        window.loginUser = new UserModel();
                        var promise = APIService.callAPI(AppConfig.apiURL + 'getHomeNews/checkLogin', 'POST', {
                            id: document.cookie.split(";")[0].split("=")[1],
                            token: document.cookie.split(";")[1].split("=")[1]
                        });
                        promise.then(function (resp) {
                            window.loginUser.set(resp);
                            self.headerView = new HeaderView({model: window.loginUser});
                            $('.header-section').html(self.headerView.el);
                        }).then(function (resp) {
                            self.personalInformationView = new PersonalInformationView();
                            $('#main-section').html(self.personalInformationView.el);
                            // new PersonalHomeBrowsingHistoryListView();
                        })
                    }
                }else {
                    this.personalInformationView=new PersonalInformationView();
                    $('#main-section').html( this.personalInformationView.el );
                }


            },
            // userInfoUpdateModal:function () {
            //     var self = this;
            //     window.loginUser = new UserModel();
            //     var promise = APIService.callAPI(AppConfig.apiURL + 'getHomeNews/checkLogin', 'POST', {
            //         id: document.cookie.split(";")[0].split("=")[1],
            //         token: document.cookie.split(";")[1].split("=")[1]
            //     });
            //     promise.then(function (resp) {
            //         window.loginUser.set(resp);
            //         self.headerView = new HeaderView({model: window.loginUser});
            //         $('.header-section').html(self.headerView.el);
            //     }).then(function (resp) {
            //         self.userInfoUpdateModalView = new UserInfoUpdateModalView({model: window.loginUser});
            //         self.userInfoUpdateModalView.render();
            //         // new PersonalHomeBrowsingHistoryListView();
            //     })
            //
            //
            // },
            userPhotoUpdateModal:function () {
                var self = this;
                window.loginUser = new UserModel();
                if(!document.cookie.split(";")[0].split("=")[1]){
                    window.location.href=AppConfig.localURL+"login.html";
                }else {
                    var promise = APIService.callAPI(AppConfig.apiURL + 'getHomeNews/checkLogin', 'POST', {
                        id: document.cookie.split(";")[0].split("=")[1],
                        token: document.cookie.split(";")[1].split("=")[1]
                    });
                    promise.then(function (resp) {
                        window.loginUser.set(resp);
                        self.headerView = new HeaderView({model: window.loginUser});
                        $('.header-section').html(self.headerView.el);
                    }).then(function (resp) {
                        self.userPhotoUpdateModalView = new UserPhotoUpdateModalView({model: window.loginUser});
                        self.userPhotoUpdateModalView.render();
                        // new PersonalHomeBrowsingHistoryListView();
                    })
                }
            }

            });

        // Returns the DesktopRouter class
        return DesktopRouter;

    }
);