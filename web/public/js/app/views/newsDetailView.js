/**
 * Created by ternence on 2017/3/19.
 */
define(["jquery", "backbone", "text!templates/NewsDetailView.html", "text!templates/CommentView.html", "collections/newsDetailCollection", "collections/commentCollection"],
    function ($, Backbone, template, CommentTemplate, NewsDetailCollection, CommentCollection) {
        var NewsDetailView = Backbone.View.extend({
            initialize: function (newsIdVal, newsAbstractVal, newsCreateTimeVal) {
                // window.newsIdVal=newsIdVal;
                // window.newsAbstractVal=newsAbstractVal;
                // window.newsCreateTimeVal=newsCreateTimeVal;
                var self = this;
                this.model = new NewsDetailCollection();
                this.modelComment = new CommentCollection();
                this.model.fetchNewsDetailDataFromServer(newsIdVal, newsAbstractVal, newsCreateTimeVal).then(function () {
                    self.modelComment.getCommentDataFromServer(newsIdVal, 1, 5, 1);
                });
                // this.commentModel = new CommentView();
                this.listenTo(this.model, 'changeNewsDetail', this.render);
                this.listenTo(this.modelComment, 'changeComment', this.renderChildren);
                // this.render();
                // this.model.on( 'change:id change:content change:is_collected', this.render, this );
            },
            events: {
                "focus .c-textarea textarea": "textareaOpen",
                "click .newsCollectBtn i": "newsCollect",
                "click .newsCollectBtn span": "newsCollect",
                "click .isLikeBtn i": "isLike",
                "click .c-submit": "commentSubmit",
                "click #allComments": "allCommentsShow",
            },
            render: function () {
                // var temp=this.model.getNewsDetailData();
                // if(this.newsDetailData.is_collected == temp.is_collected){
                //
                // }

                this.newsDetailData = this.model.getNewsDetailData();
                this.comment_num = this.newsDetailData.comment_num;
                this.template = _.template(template);
                this.$el.html(this.template(this));
                if (Number($('.newsCollectBtn').attr("is_collected"))) {
                    $('.newsCollectBtn').find("i").removeClass("fa-star-o");
                    $('.newsCollectBtn').find("i").addClass("fa-star");
                } else {
                    $('.newsCollectBtn').find("i").removeClass("fa-star");
                    $('.newsCollectBtn').find("i").addClass("fa-star-o");
                }
                this.renderChildren();
                return this;
            },
            renderChildren: function () {
                this.comments = this.modelComment.getCommentData();
                if (this.comments) {
                    this.CommentTemplate = _.template(CommentTemplate);
                    $('#comment-section').html(this.CommentTemplate(this));
                    if (this.comments) {
                        _.each(this.comments.attributes.comments, function (item) {
                            if (Number($('#isLikeBtn' + item.id).attr("isLikeVal"))) {
                                $('#isLikeBtn' + item.id).find("i").removeClass("fa-thumbs-o-up");
                                $('#isLikeBtn' + item.id).find("i").addClass("fa-thumbs-up");
                            } else {
                                $('#isLikeBtn' + item.id).find("i").removeClass("fa-thumbs-up");
                                $('#isLikeBtn' + item.id).find("i").addClass("fa-thumbs-o-up");
                            }
                        })

                    }
                }

                return this;
            },
            textareaOpen: function (e) {
                $(e.currentTarget).parent().parent().addClass('focus');
            },
            newsCollect: function (e) {
                var self = this;
                var nid = $(e.currentTarget).parent().attr("nidVal");
                var is_collected = $(e.currentTarget).parent().attr("is_collected");
                Number(is_collected) ? type = 1 : type = 0;
                this.model.collectNewToServer(nid, type, newsIdVal, newsAbstractVal, newsCreateTimeVal)
            },
            isLike: function (e) {
                var cid = $(e.currentTarget).parent().attr("commentIdVal");
                var is_like = $(e.currentTarget).parent().attr("isLikeVal");
                Number(is_like) ? type = 1 : type = 0;
                this.modelComment.sendLickCommentToServer(cid, type, newsIdVal);
            },
            commentSubmit: function () {
                if ($('#commentTextarea').val()) {
                    var timestamp = new Date().getTime();
                    var content = $('#commentTextarea').val();
                    this.modelComment.sendCommentDataToServer(newsIdVal, content, timestamp).then(function () {
                        return true;
                    })
                } else {
                    alert("评论不能为空")
                }

            },
            allCommentsShow: function () {
                this.modelComment.getCommentDataFromServer(newsIdVal, 1, this.comment_num, 1);
            },
        });
        return NewsDetailView;

    })