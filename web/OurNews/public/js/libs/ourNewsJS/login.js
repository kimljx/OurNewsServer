/**
 * Created by ternence on 2017/3/3.
 */
$(function () {
    var vc = new LoginController($('div.log-row'));
    vc.viewLoaded();
    window.mainViewController = vc;

});
// $(document).ready(function(){
//     $("#txtUserName").focus('input propertychange', function () {
//         if ($('#txtUserName').val() == '') {
//             $('#txtUserName').next().next().text('请输入您的账号！');
//             $('#txtUserName').parent().addClass('has-error');
//         } else if ($('#txtPassword').val() == '') {
//             $('#txtPassword').next().text('请输入您的密码！');
//             $('#txtPassword').parent().addClass('has-error');
//         }else{
//             $('#txtPassword').next().text('登录');
//         }
//     });
// });

    var LoginController = function (uiElement) {
    this.uiElement = uiElement;
};
LoginController.prototype.viewLoaded = function () {
    var self = this;
    $('#btnLogin').click(function (evt) {
        evt.preventDefault();
        evt.stopPropagation();
        if ($(this).hasClass('disabled')) {
            return;
        }
        $('#statusView').text('');
        $('#statusView').hide();
        if (self.validate()) {
            self.login();
        }
        return false;
    });
}
LoginController.prototype.validate = function () {
    $('#txtUserName').parent().removeClass('has-error');
    $('#txtUserName').next().text('');
    $('#txtPassword').parent().removeClass('has-error');
    $('#txtPassword').next().text('');
    var isValid = true;
    if ($('#txtUserName').val() == '') {
        $('#txtUserName').next().next().text('请输入您的账号！');
        $('#txtUserName').parent().addClass('has-error');
        isValid = false;
    } else if ($('#txtPassword').val() == '') {
        $('#txtPassword').next().text('请输入您的密码！');
        $('#txtPassword').parent().addClass('has-error');
        isValid = false;
    }
    return isValid;
}

LoginController.prototype.login = function () {
    var self = this;
    var timestamp=new Date().getTime();
    $('#btnLogin').addClass('disabled');
    $('#btnLogin').text('登录...');
    Site.callAPI(AppConfig.apiURL+'login', 'POST', {
        login_name: $('#txtUserName').val(),
        password: $.md5("a0f33a00f12b4a8eb6f626f03a1f140a"+$('#txtPassword').val()+timestamp),
        time:timestamp,
    }, function (resp) {
        //$('#btnLogin').removeClass('disabled');
        //$('#btnLogin').text('登录');
        // localStorage.setItem('__SID__', data.SID);
        // self.loadingUserInfo();
        if(resp.data.id){
            // exp.setTime(exp.getTime() + 3 * 24 * 60 * 60 * 1000);
            var Days = 30;
            var exp = new Date();
            exp.setTime(exp.getTime() + Days*24*60*60*1000);
            document.cookie = "id="+resp.data.id + ";expires=" + exp.toGMTString()+ ";path=/";
            document.cookie = "token=" + resp.data.token + ";expires=" + exp.toGMTString()+ ";path=/";
            window.location.href=AppConfig.localURL+"index.html";
        }else if(resp.result=="error"){
            alert("用户名或密码错误，登录失败！")
        }
    }, function (error) {
        $('#btnLogin').removeClass('disabled');
        $('#btnLogin').text('登录');
        alert('请求错误：'+error_code)
    });
}
// LoginController.prototype.loadingUserInfo = function () {
//     //Loading login user info
//     Site.callAPI(/*Site.apiURL + 'auth/getLoginInfo'*/'http://112.74.52.72:8080/OurNews/login', 'GET', {"__SID__": localStorage.getItem('__SID__')}, function (data) {
//         $('#btnLogin').removeClass('disabled');
//         $('#btnLogin').text('登录');
//         if (data.permission.admin) {
//             window.location.href = 'admin/client.php';
//         } else if (data.permission.client) {
//             window.location.href = 'client/index.html';
//         } else {
//             alert('Your account has problem, please contact administrator');
//         }
//
//     }, function (error, result) {
//         $('#btnLogin').removeClass('disabled');
//         $('#btnLogin').text('登录');
//         //Has not login
//         alert(error);
//     });
// }
$("#txtUserName").bind('input propertychange', function () {
    if ($('#txtUserName').val() == '') {
        $('#txtUserName').next().next().text('请输入您的账号！');
        $('#txtUserName').parent().addClass('has-error');
    } else if ($('#txtPassword').val() == '') {
        $('#txtPassword').next().text('请输入您的密码！');
        $('#txtPassword').parent().addClass('has-error');
    }else{
        $('#txtPassword').next().text('登录');
    }
});
$("#txtPassword").bind('input propertychange', function () {
    if ($('#txtUserName').val() == '') {
        $('#txtUserName').next().next().text('请输入您的账号！');
        $('#txtUserName').parent().addClass('has-error');
    } else if ($('#txtPassword').val() == '') {
        $('#txtPassword').next().text('请输入您的密码！');
        $('#txtPassword').parent().addClass('has-error');
    }else{
        $('#txtPassword').next().text('登录');
    }
});