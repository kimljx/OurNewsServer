/**
 * Created by ternence on 2017/3/23.
 */
define(["jquery", "backbone", 'webuploader', "collections/userInfoUpdateModalCollection", "text!templates/UserPhotoUpdateModalView.html"],
    function ($, Backbone, WebUploader, UserInfoUpdateModalCollection, template) {
        var UserPhotoUpdateModalView = Backbone.View.extend({
            // The DOM Element associated with this view
            // el: "#main-section",
            // View constructor
            initialize: function () {
                var $this = this;
                debugger;
                // !options && (options = {title: '...'});
                this.template = _.template(template);
                this.setElement(this.template());
                $(this.el).on('hidden.bs.modal', function () {
                    $this.remove();
                    window.history.back();
                });
            },
            events: {},
            render: function () {
                debugger;
                var self = this;
                $('body').append($(this.el).modal('show'));
                setTimeout(function () {
                    self._initUploader();
                    setTimeout(function () {
                        $('.webuploader-pick').next().find("input").addClass("hide");
                    }, 1);
                }, 500);
                return this;
            },
            _initUploader: function () {
                var self = this;
                debugger;
                var uploader = WebUploader.create({
                    swf: '../libs/webuploader/Uploader.swf',
                    server: AppConfig.apiURL + 'uploadImage',
                    pick: this.$('.uploderPhoto'),
                    auto: true,
                    duplicate: true,
                    // accept: {
                    // 	title: 'File',
                    // 	extensions: 'csv',
                    // 	mimeTypes:".csv"
                    // }
                });
                uploader.on('fileQueued', function (file) {
                    debugger;
                    self.uploadResourceID = false;
                });
                uploader.on('uploadProgress', function (file, percentage) {
                    debugger;
                });
                uploader.on('uploadSuccess', function (file, response) {
                    debugger;
                    if (response.status == 0) {
                        self.uploadResourceID = response.data;
                    } else {
                        self.uploadResourceID = false;
                        alert(response.error);
                    }
                    $('.company-logo').css("border", "1px solid #ffffff");

                });

                uploader.on('uploadError', function (file) {
                    debugger;
                    alert('Uploading file exception!');
                });

                uploader.on('uploadComplete', function (file) {
                    debugger;
                    if (!self.uploadResourceID) {
                        return;
                    }
                    self.$('.company-logo').css("background-image", "url(/api/resource/getImageResource/" + self.uploadResourceID + ")");
                    self.$('.company-logo').css("background-position", "8px 30px");
                    self.$('.company-logo').css("background-size", "90% 65%");
                });
            },
        })

        // Returns the View class
        return UserPhotoUpdateModalView;

    }
);