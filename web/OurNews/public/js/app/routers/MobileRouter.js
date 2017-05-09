// MobileRouter.js
// ---------------
define([
        "jquery",
        "backbone",
        "models/homeNewsModel",
        "models/personalHomePageModel",
        "views/homeNewsView",
        "views/personalHomePageView",
        "collections/homeNewsCollection"
    ],

    function ($, Backbone, HomeNewsModel,PersonalHomePageModel, HomeNewsView, PersonalHomePageView) {

        var MobileRouter = Backbone.Router.extend({

            initialize: function() {

                // Tells Backbone to start watching for hashchange events
                Backbone.history.start();

            },

            // All of your Backbone Routes (add more)
            routes: {
                
                // When there is no hash bang on the url, the home method is called
                "": "index",
                "homepage": "homepage"


            },

            index: function() {

                // Instantiates a new view which will render the header text to the page
                new PersonalHomePageView();

            },
            homepage: function () {
                new HomeNewsView();

            }
        });

        // Returns the MobileRouter class
        return MobileRouter;

    }

);