// /**
//  * Created by ternence on 2017/3/18.
//  */
// define(['jquery', 'backbone', 'text!templates/PersonalHomeBrowsingHistoryAndCollectionView.html','collections/personalHomeBrowsingHistoryAndCollectionCollection'],
//     function ($, Backbone, template,PersonalHomeBrowsingHistoryAndCollectionCollection) {
//         var PersonalHomeBrowsingHistoryAndCollectionView = Backbone.View.extend({
//             el: '#BrowsingHistoryAndCollection',
//             initialize: function () {
//                 debugger;
//                 this.model=new PersonalHomeBrowsingHistoryAndCollectionCollection();
//                 this.listenTo(this.model,'change',this.render);
//             },
//             events: {
//             },
//             render: function () {
//                 debugger;
//                 this.dataList=this.model.getBrowsingHistoryAndCollectionNewsData();
//                 this.template = _.template(template);
//                 this.$el.html(this.template(this));
//             },
//
//         });
//         return PersonalHomeBrowsingHistoryAndCollectionView;
//     })