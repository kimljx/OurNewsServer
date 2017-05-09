/**
 * Created by ternence on 2017/3/22.
 */
define(["jquery", "backbone", "models/personalHomeBrowsingHistoryAndCollectionModel"],

    function ($, Backbone, PersonalHomeBrowsingHistoryAndCollectionModel) {
        // Creates a new Backbone Collection class object
        var PersonalHomeBrowsingHistoryAndCollectionCollection = Backbone.Collection.extend({

                // Tells the Backbone Collection that all of it's models will be of type Model (listed up top as a dependency)
                model: PersonalHomeBrowsingHistoryAndCollectionModel,
                initialize: function () {
                    this.fetchBrowsingHistoryNewsDataFromServer(1, 5, 1);
                },
                getBrowsingHistoryAndCollectionNewsData: function () {
                    return this.models;
                },
                fetchBrowsingHistoryNewsDataFromServer: function (page, size, sort) {
                    var self = this;
                    this.models = [];
                    return APIService.callAPI(AppConfig.apiURL + 'getHistory', 'POST', {
                        uid: document.cookie.split(";")[0].split("=")[1],
                        oid: document.cookie.split(";")[0].split("=")[1],
                        token: document.cookie.split(";")[1].split("=")[1],
                        page: page,
                        size: size,
                        sort: sort
                    }).then(function (resp) {
                        _.each(resp.news, function (item) {
                            var personalHomeBrowsingHistoryAndCollectionModel = new PersonalHomeBrowsingHistoryAndCollectionModel();
                            personalHomeBrowsingHistoryAndCollectionModel.id = item.id;
                            personalHomeBrowsingHistoryAndCollectionModel.title = item.title;
                            personalHomeBrowsingHistoryAndCollectionModel.cover = item.cover;
                            personalHomeBrowsingHistoryAndCollectionModel.abstract = item.abstract;
                            personalHomeBrowsingHistoryAndCollectionModel.createtime = item.create_time;
                            personalHomeBrowsingHistoryAndCollectionModel.type = item.type;
                            personalHomeBrowsingHistoryAndCollectionModel.manager = item.manager;
                            personalHomeBrowsingHistoryAndCollectionModel.comment_num = item.comment_num;
                            personalHomeBrowsingHistoryAndCollectionModel.history_num = item.history_num;
                            self.models.push(personalHomeBrowsingHistoryAndCollectionModel);
                        })
                        self.trigger('change');
                    })
                    // self.trigger('newsChange');

                },
                fetchCollectionNewsDataFromServer: function (page, size, sort) {
                    var self = this;
                    this.models = [];
                    return APIService.callAPI(AppConfig.apiURL + 'getCollections', 'POST', {
                        uid: document.cookie.split(";")[0].split("=")[1],
                        oid: document.cookie.split(";")[0].split("=")[1],
                        token: document.cookie.split(";")[1].split("=")[1],
                        page: page,
                        size: size,
                        sort: sort
                    }).then(function (resp) {
                        _.each(resp.news, function (item) {
                            var personalHomeBrowsingHistoryAndCollectionModel = new PersonalHomeBrowsingHistoryAndCollectionModel();
                            personalHomeBrowsingHistoryAndCollectionModel.id = item.id;
                            personalHomeBrowsingHistoryAndCollectionModel.title = item.title;
                            personalHomeBrowsingHistoryAndCollectionModel.cover = item.cover;
                            personalHomeBrowsingHistoryAndCollectionModel.abstract = item.abstract;
                            personalHomeBrowsingHistoryAndCollectionModel.createtime = item.create_time;
                            personalHomeBrowsingHistoryAndCollectionModel.type = item.type;
                            personalHomeBrowsingHistoryAndCollectionModel.manager = item.manager;
                            personalHomeBrowsingHistoryAndCollectionModel.comment_num = item.comment_num;
                            personalHomeBrowsingHistoryAndCollectionModel.history_num = item.history_num;
                            self.models.push(personalHomeBrowsingHistoryAndCollectionModel);
                        })
                        self.trigger('change');
                    })
                    // self.trigger('newsChange');

                },

            })
            ;

// Returns the Model class
        return PersonalHomeBrowsingHistoryAndCollectionCollection;

    }
)
;