/**
 * Created by ternence on 2017/3/15.
 */
define(["jquery", "backbone", "models/headlinesNewsModel"],

    function ($, Backbone, HeadlinesNewsModel) {
        // Creates a new Backbone Collection class object
        var HeadlinesNewsCollection = Backbone.Collection.extend({

            // Tells the Backbone Collection that all of it's models will be of type Model (listed up top as a dependency)
            model: HeadlinesNewsModel,
            initialize: function () {
                this.fetchHeadlinesNewsDataFromServer();
            },
            getHeadlinesNewsData: function () {
                return this.models;
            },
            fetchHeadlinesNewsDataFromServer: function () {
                var self = this;
                return APIService.callAPI(AppConfig.apiURL+'getNewList', 'POST', {
                    type: 5,
                    page: 1,
                    size: 10,
                    sort: 1
                }).then(function (resp) {
                    _.each(resp.news, function (item) {
                        var headlinesNewsModel = new HeadlinesNewsModel();
                        headlinesNewsModel.id = item.id;
                        headlinesNewsModel.title = item.title;
                        headlinesNewsModel.cover = item.cover;
                        headlinesNewsModel.abstract = item.abstract;
                        headlinesNewsModel.createtime = item.create_time;
                        headlinesNewsModel.type = item.type;
                        headlinesNewsModel.manager = item.manager;
                        headlinesNewsModel.comment_num = item.comment_num;
                        headlinesNewsModel.history_num = item.history_num;
                        self.models.push(headlinesNewsModel);
                    })
                    self.trigger('change');
                })

            }

        });

        // Returns the Model class
        return HeadlinesNewsCollection;

    }
);