// Model.js
// --------
define(["jquery", "backbone"],

    function ($, Backbone) {
        // Creates a new Backbone Model class object
        var HomeNewsModel = Backbone.Model.extend({

                // Model Constructor
                initialize: function () {
                    // this.fetchAllHomeNewsDataFromServer();
                },

                // Default values for all of the Model attributes
                defaults: {
                    type: 0,
                    list: [{
                        id: "",
                        title: "",
                        cover: "",
                        abstract: "",
                        createtime: "",
                        type: 0,
                        manager: {
                            id: 0,
                            nick_name: '',
                            sex: 0,
                            sign: '',
                            birthday: 0,
                            photo: ''
                        },
                        comment_num:0,
                        history_num:1,
                    }]

                },

                // Gets called automatically by Backbone when the set and/or save methods are called (Add your own logic)
                validate: function (attrs) {
                }
                ,
                // fetchAllHomeNewsDataFromServer: function () {
                //     debugger;
                //     return APIService.callAPI('http://112.74.52.72:8080/OurNews/getHomeNews').then(function (resp) {
                //         var data=resp;
                //     })
                //
                // }

            })
            ;

        // Returns the Model class
        return HomeNewsModel;

    }
);
