/**
 * Created by ternence on 2017/3/19.
 */
define(["jquery","backbone","text!templates/PersonalHomeBrowsingHistoryListView.html"],
    function ($,Backbone,template) {
    var PersonalHomeBrowsingHistoryListView=Backbone.View.extend({
        el:'#relatedFeed',
        initialize:function () {
            this.render();
        },
        events:{

        },
        render:function () {
            this.template=_.template(template);
            this.$el.html(this.template(this));
        },
    });
    return PersonalHomeBrowsingHistoryListView;
})