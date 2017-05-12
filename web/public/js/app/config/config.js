// Require.js Configurations
// -------------------------
require.config({

    // Sets the js folder as the base directory for all future relative paths
    baseUrl: "./public/js/app",

    // 3rd party script alias names (Easier to type "jquery" than "libs/jquery, etc")
    // probably a good idea to keep version numbers in the file names for updates checking
    paths: {

        // Core Libraries
        // --------------
        "jquery": "../libs/jquery",

        "jqueryui": "../libs/jqueryui",

        "jquerymobile": "../libs/jquery.mobile",

        "underscore": "../libs/lodash",

        // "jquery.md5": "../libs/jquery.md5",

        "backbone": "../libs/ourNewsJS/backbone",

        // Plugins
        // -------
        "backbone.validateAll": "../libs/plugins/Backbone.validateAll",

        "bootstrap": "../libs/plugins/bootstrap",

        "text": "../libs/plugins/text",

        "jasminejquery": "../libs/plugins/jasmine-jquery",

        "scripts": "../libs/ourNewsJS/scripts",

        "jqueryMd5": "../libs/jquery.md5",

        "urlConfig":"../libs/ourNewsJS/urlConfig",

        "APIservice":"../../../server/APIservice",

        "bootstrap-datetimepicker":"../libs/bootstrap-datetimepicker.min",

        "webuploader":"../libs/webuploader/webuploader",

        "jqueryCookie":"../libs/jquery.cookie",

        // "domReady":"../libs/domReady",

        // "firstPageView":"views/firstPageView"

        // "slidebars":"../../lib/theme/js/slidebars.min"
    },

    // Sets the configuration for your third party scripts that are not AMD compatible
    shim: {

        // jQuery Mobile
        "jquerymobile": ["jquery"],

        // Twitter Bootstrap jQuery plugins
        "bootstrap": ["jquery"],

        // jQueryUI
        "jqueryui": ["jquery"],

        // Backbone.validateAll plugin that depends on Backbone
        "backbone.validateAll": ["backbone"],

        // Jasmine-jQuery plugin
        "jasminejquery": ["jquery"],

        "scripts": ["jquery"],

        "APIservice":["urlConfig"],

        "jqueryMd5":["jquery"],

        "bootstrap-datetimepicker":["bootstrap"],

    }

});

require(['jquery', /*'domReady',*/ 'scripts','urlConfig','APIservice','jqueryMd5','bootstrap-datetimepicker','jqueryCookie','webuploader'],
    function($, scripts,urlConfig,APIservice,jqueryMd5,bootstrapDatetimepicker,jqueryCookie,WebUploader){
})

// require([
//     // 'header',
//     'DesktopRouter',
//     'utils',
//     'firstPageView',
//     'homeView',
//     'footerView',
//     'orderMenusView',
//     'addFoodView',
//     'userModel',
// ], function(){
//     templateLoader.load(["FirstPageView", ],
//         function () {
//         debugger;
//             app = new Router();
//             Backbone.history.start();
//         });
// })

// require(['jquery','domReady','scripts'],function($){    //功能代码        }

/**
* FIXES: BUG-2065 "Uncaught Error: Script error for ..."
*
* REASON FOR BUG: The internet going out momentarily
* 	after the initial scripts have loaded and have begun
* 	loading the chain of javascript-loaded scripts causes
* 	requirejs to throw the Script Errors because it's unable
* 	to reach the files it's being told to load.
*
* TO REPRODUCE: in chrome, throttle the internet down then
* 	turn off the internet once the reload button shows on
* 	the login screen (and the stop button hides). This ensures
* 	that requirejs has been loaded and is loading other scripts.
*/
require.onError = function( err ) {
    if ( err.message.indexOf( 'Script error for' ) != -1 ) {
        if ( require.script_error_shown == undefined ) {
            require.script_error_shown = 1;
            //There is no pretty errorbox at this time. You may only have basic javascript.
            if ( confirm( "Unable to download required data. Your internet connection may have failed press Ok to reload." ) ) {
                //For testing, so that there's time to turn internet back on after confirm is clicked.
                //window.setTimeout(function(){window.location.reload()},5000);
                window.location.reload();
            }
        }
        console.debug( err.message );
        //Stop error from bubbling up.
        delete err;
    }
};