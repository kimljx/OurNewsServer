/**
 * Created by ternence on 2017/3/19.
 */
define(["jquery", "backbone"],

    function ($, Backbone) {
        // Creates a new Backbone Model class object
        var NewsDetailModel = Backbone.Model.extend({

            // Model Constructor
            initialize: function () {
            },

            // Default values for all of the Model attributes
            defaults: {

                        id: '',
                        content: '',
                        contentList: '',
                        is_collected: 0,
                        comment_num: 0,
                        history_num: 0,
                        collection_num: 0,
                        newsAbstractVal: '',
                        newsCreateTimeVal: '',
                        manager: {
                            id: 0,
                            nick_name: '',
                            sex: 0,
                            sign: '',
                            birthday: 0,
                            photo: ''
                        }

            },

            // Gets called automatically by Backbone when the set and/or save methods are called (Add your own logic)
            validate: function (attrs) {
            }

        });

        // Returns the Model class
        return NewsDetailModel;

    }
);