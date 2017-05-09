/**
 * Created by ternence on 2017/3/22.
 */
define(["jquery", "backbone", "collections/personalInformationCollection", "text!templates/PersonalInformationView.html"],

    function ($, Backbone, PersonalInformationCollection, template) {
        var PersonalInformationView = Backbone.View.extend({

            // The DOM Element associated with this view

            // View constructor
            initialize: function () {
                // Calls the view's render method
                this.model=new PersonalInformationCollection();
                this.listenTo(this.model,'change',this.render);
            },

            // View Event Handlers
            events: {
                "mouseover .avator-mode img": "mouseoverPhoto",
                "mouseover .avator-mode div.update-avator": "mouseoverUpdateAvator",
                "mouseout .avator-mode img": "mouseoutPhoto",
                "mouseout .avator-mode div.update-avator": "mouseoutUpdateAvator",
            },

            // Renders the view's template to the UI
            render: function () {
                this.userInfoData=this.model.getUserInfoData();
                // Setting the view's template property using the Underscore template method
                this.template = _.template(template);
                // Dynamically updates the UI with the view's template
                this.$el.html(this.template(this));
                // Maintains chainability
                return this;

            },
            mouseoverPhoto: function () {
                $('.avator-mode div.update-avator').css("bottom", "0");
            },
            mouseoutPhoto: function () {
                $('.avator-mode div.update-avator').css("bottom", "-32px");

            },
            mouseoverUpdateAvator: function () {
                $('.avator-mode div.update-avator').css("bottom", "0");
            },

            mouseoutUpdateAvator:function () {
                $('.avator-mode div.update-avator').css("bottom", "-32px");
            },


        });

        // Returns the View class
        return PersonalInformationView;

    }
);