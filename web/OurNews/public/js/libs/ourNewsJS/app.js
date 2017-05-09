/**
 * Created by ternence on 2017/3/3.
 */
var Site = function(){};
Site.apiURL = AppConfig.apiURL;
Site.loacalURL = AppConfig.loacalURL;
Site.purveyorList = [];
Site.getPurveyorById = function(id)
{
    for(var i=0; i < Site.purveyorList.length;i++)
    {
        if(Site.purveyorList[i].id == id){
            return Site.purveyorList[i];
        }
    }
    return null;
}
Site.showLoading = function(targetView)
{
    if(!targetView)
    {
        targetView = $('.panel');
    }
    $("<div class='refresh-block'><span class='refresh-loader'><i class='fa fa-spinner fa-spin'></i></span></div>").appendTo(targetView);
}
Site.hideLoading = function()
{
    $('.refresh-block').remove();
}
Site.showStatusMessage = function(content,type,uiElement)
{
    if(!type)
    {
        type = 'info';
    }
    if(!uiElement)
    {
        uiElement = $('#statusMessage');
    }
    uiElement.html(content);
    uiElement.attr('class','alert alert-'+type)
    uiElement.show();
}
Site.hideStatusMessage = function(uiElement)
{
    if(!uiElement)
    {
        uiElement = $('#statusMessage');
    }
    uiElement.hide();
}
Site.htmlEncode = function(value){
    return $('<div/>').text(value).html();
}
Site.htmlDecode = function(value){
    return $('<div/>').html(value).text();
}
Site.callAPI = function(url,method,data,cb,cb_error)
{
    $.ajax({
        type: method,
        url: url ,
        data: data ,
        dataType: 'json',
        success: function(result){
            if(result.result == "success" )
            {
                cb(result);
            }else{
                cb_error(result,result);
            }
        } ,
        error : function(error) {
            cb_error('Inner error, please try again later!')
        }
    });
}
Site.callResourceListAPI = function(name,urlParameter,cb,cb_error)
{
    var url = Site.apiURL+name;
    if(urlParameter)
    {
        url += "?"+urlParameter;
    }
    Site.callAPI(url,'GET',null,cb,cb_error);
}
Site.callResourceCreateAPI = function(name,data,cb,cb_error)
{
    Site.callAPI(Site.apiURL+name,'POST',data,cb,cb_error);
}
Site.callResourceUpdateAPI = function(name,data,cb,cb_error)
{
    Site.callAPI(Site.apiURL+name+"/"+data.id,'PUT',data,cb,cb_error);
}
Site.callResourceDeleteAPI = function(name,id,cb,cb_error)
{
    Site.callAPI(Site.apiURL+name+"/"+id,'DELETE',null,cb,cb_error);
}
Site.getParameter = function(name) {
    var url = document.location.href;
    var start = url.indexOf("?")+1;
    if (start==0) {
        return "";
    }
    var value = "";
    var queryString = url.substring(start);
    var paraNames = queryString.split("&");
    for (var i=0; i<paraNames.length; i++) {
        if (name==Site.getParameterName(paraNames[i])) {
            value = Site.getParameterValue(paraNames[i])
        }
    }
    return value;
}
Site.getParameterName = function(str) {
    var start = str.indexOf("=");
    if (start==-1) {
        return str;
    }
    return str.substring(0,start);
}

Site.getParameterValue = function(str) {
    var start = str.indexOf("=");
    if (start==-1) {
        return "";
    }
    return str.substring(start+1);
}