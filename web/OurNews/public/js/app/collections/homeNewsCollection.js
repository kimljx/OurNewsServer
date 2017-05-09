// Collection.js
// -------------
define(["jquery", "backbone", "models/homeNewsModel"],

    function ($, Backbone, HomeNewsModel) {
        // Creates a new Backbone Collection class object
        var HomeNewsCollection = Backbone.Collection.extend({

            // Tells the Backbone Collection that all of it's models will be of type Model (listed up top as a dependency)
            model: HomeNewsModel,
            initialize: function () {
                this.fetchAllHomeNewsDataFromServer();
            },
            getHomeNewsData: function () {
                return this.models;
            },
            fetchAllHomeNewsDataFromServer: function () {
                var self = this;
                return APIService.callAPI(AppConfig.apiURL + 'getHomeNews', 'POST').then(function (resp) {
                    _.each(resp.news, function (item) {
                        var homeNewsModel = new HomeNewsModel();
                        homeNewsModel.type = item.type;
                        var list = [];

                        _.each(item.list, function (listItem) {
                            list.push({
                                id: listItem.id,
                                title: listItem.title,
                                cover: listItem.cover,
                                abstract: listItem.abstract,
                                createtime: listItem.create_time,
                                type: listItem.type,
                                manager: listItem.manager,
                                comment_num:listItem.comment_num,
                                history_num:listItem.history_num
                            })
                        })
                        homeNewsModel.list = list;
                        self.models.push(homeNewsModel);
                    })
                    // self.trigger('newsChange');
                    self.trigger('change');
                })

            }

        });

        // Returns the Model class
        return HomeNewsCollection;

    }
);