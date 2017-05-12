/**
 * Created by ternence on 2017/3/21.
 */
define(["jquery", "backbone"],

    function ($, Backbone) {
        // Creates a new Backbone Model class object
        var CommentModel = Backbone.Model.extend({

            // Model Constructor
            initialize: function () {
            },

            // Default values for all of the Model attributes
            defaults: {
                comments: [
                    {
                        id: 0,
                        content: '',
                        create_time: '',
                        user: {
                            id: 0,
                            nick_name: '',
                            sex: 0,
                            sign: '',
                            birthday: '',
                            photo: ''
                        },
                        like_num: 0,
                        is_like: 0,
                        comment_children: [
                            {
                                id: 0,
                                content: '',
                                create_time: '',
                                user: {
                                    id: 0,
                                    nick_name: '',
                                    sex: 0,
                                    sign: '',
                                    birthday: '',
                                    photo: ''
                                }
                            }
                        ]
                    }
                ]
            },

            // Gets called automatically by Backbone when the set and/or save methods are called (Add your own logic)
            validate: function (attrs) {
            }

        });

        // Returns the Model class
        return CommentModel;

    }
);
