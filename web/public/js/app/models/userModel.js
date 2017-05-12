/**
 * Created by ternence on 2017/3/16.
 */
define(["jquery", "backbone"],

    function($, Backbone) {
        // Creates a new Backbone Model class object
        var UserModel = Backbone.Model.extend({

            // Model Constructor
            initialize: function() {
            },

            // Default values for all of the Model attributes
            defaults: {
                id: '',
                login_name: '',
                nick_name: '',
                sex: 0,
                sign: '',
                birthday: 0,
                photo: '',
            },

            // Gets called automatically by Backbone when the set and/or save methods are called (Add your own logic)
            validate: function(attrs) {
            }

        });

        // Returns the Model class
        return UserModel;

    }

);
