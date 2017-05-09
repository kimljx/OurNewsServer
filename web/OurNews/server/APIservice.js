/**
 * Created by ternence on 2017/3/3.
 */
var APIService = function(){};
APIService.callAPI = function(url,method,data)
{
    // if(!method){
    //     method = 'GET';
    // }
    // if(!data){
    //     data = {'timestamp':(new Date()).getTime()};
    // }
    // if(localStorage.getItem('__SID__'))
    // {
    //     data.__SID__ = localStorage.getItem('__SID__');
    // }
    return new Promise(function( resolve, reject ) {
        $.ajax({
            type: method,
            // url: AppConfig.apiURL+url ,
            url:url,
            data: data ,
            dataType: 'json',
            success: function(resp){
                if ( resp.result == "success" ) { //successfully
                    resolve(resp.data) //just return the data
                } else { // failed
                    reject( resp.error_code ) // return error to reject
                }
            } ,
            error : function(resp) {
                reject(resp)
            }
        });
    });
}