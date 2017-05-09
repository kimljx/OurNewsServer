/**
 * Created by ternence on 2017/3/3.
 */
define(["jquery", "backbone", "models/personalHomePageModel", "text!templates/PersonalHomePageView.html", "text!templates/PersonalHomeBrowsingHistoryAndCollectionView.html","collections/personalHomeBrowsingHistoryAndCollectionCollection"],

    function($, Backbone, PersonalHomePageModel, template,templateHistoryAndCollection,PersonalHomeBrowsingHistoryAndCollectionCollection){
        var PersonalHomePageView = Backbone.View.extend({

            // The DOM Element associated with this view

            // View constructor
            initialize: function() {
                // Calls the view's render method
                this.render();
                this.modelHistoryAndCollection=new PersonalHomeBrowsingHistoryAndCollectionCollection();
                this.listenTo(this.modelHistoryAndCollection,'change',this.renderHistoryAndCollection);

            },

            // View Event Handlers
            events: {
                "click .tab-0 li": "tabItemClick",
                "click a.link":"aLinkClick"
            },

            // Renders the view's template to the UI
            render: function() {

                // Setting the view's template property using the Underscore template method
                this.template = _.template(template);
                // Dynamically updates the UI with the view's template
                this.$el.html(this.template(this));
                // Maintains chainability
                return this;

            },
            renderHistoryAndCollection: function () {
                this.dataList=this.modelHistoryAndCollection.getBrowsingHistoryAndCollectionNewsData();
                this.templateHistoryAndCollection = _.template(templateHistoryAndCollection);
                $('#BrowsingHistoryAndCollection').html(this.templateHistoryAndCollection(this));
            },
            tabItemClick: function (e) {
                $('.tab').find('li.active').removeClass('active');
                $(e.currentTarget).addClass('active');
                switch ($(e.currentTarget).attr('code')) {
                    case "1":
                        $(e.currentTarget).parent().removeClass('secondTab');
                        $(e.currentTarget).parent().addClass('firstTab');
                        this.modelHistoryAndCollection.fetchBrowsingHistoryNewsDataFromServer(1,5,1);
                        break;
                    case "2":
                        $(e.currentTarget).parent().removeClass('firstTab');
                        $(e.currentTarget).parent().addClass('secondTab');
                        this.modelHistoryAndCollection.fetchCollectionNewsDataFromServer(1,10,1);
                        break;
                    // default:$(".main").css({"background-color":"#fff"});break;
                }
            },
            aLinkClick:function (e) {
                window.newsIdVal=$(e.currentTarget).attr("newsIdVal");
                window.newsAbstractVal=$(e.currentTarget).attr("newsAbstractVal");
                window.newsCreateTimeVal=$(e.currentTarget).attr("newsCreateTimeVal");
            }

        });

        // Returns the View class
        return PersonalHomePageView;

    }

);