/**
 * Created by ternence on 2017/3/15.
 */
define(["jquery", "backbone", "collections/headlinesNewsCollection", "text!templates/HeadlinesNewsView.html"],
    function ($, Backbone, HeadlinesNewsCollection, template) {
        var HeadlinesNewsView = Backbone.View.extend({
            // The DOM Element associated with this view
            // View constructor
            initialize: function () {
                this.model=new HeadlinesNewsCollection();
                // Calls the view's render method
                this.listenTo(this.model,'change',this.render);
                //  this.model.on('change',this.render(),this);
                // this.model.bind('change:id change:title change:cover change:abstract change:createtime change:type',this.render());

            },

            // View Event Handlers
            events: {
                "click a.link":"aLinkClick"
            },

            // Renders the view's template to the UI
            render: function () {
                this.dataList=this.model.getHeadlinesNewsData();
                // Setting the view's template property using the Underscore template method
                this.template = _.template(template);

                // Dynamically updates the UI with the view's template
                this.$el.html(this.template(this));

                // Maintains chainability
                return this;

            },
            aLinkClick:function (e) {
                window.newsIdVal=$(e.currentTarget).attr("newsIdVal");
                window.newsAbstractVal=$(e.currentTarget).attr("newsAbstractVal");
                window.newsCreateTimeVal=$(e.currentTarget).attr("newsCreateTimeVal");
            }

        });

        // Returns the View class
        return HeadlinesNewsView;

    }
);