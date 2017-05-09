/**
 * Created by ternence on 2017/3/18.
 */
$(function () {
    var vc = new RegistrationController($('div.log-row'));
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
//             $('#txtPassword').next().text('注册');
//         }
//     });
// });

var RegistrationController = function (uiElement) {
    this.uiElement = uiElement;
};
RegistrationController.prototype.viewLoaded = function () {
    var self = this;
    $('#btnRegistration').click(function (evt) {
        evt.preventDefault();
        evt.stopPropagation();
        if ($(this).hasClass('disabled')) {
            return;
        }
        $('#statusView').text('');
        $('#statusView').hide();
        if (self.validate()) {
            self.registration();
        }
        return false;
    });
}
RegistrationController.prototype.validate = function () {
    var validatePass=false;
    $('#checkbox1').parent().parent().removeClass('has-error');
    $('#txtUserName').parent().removeClass('has-error');
    $('#txtUserName').next().text('');
    $('#txtPassword').parent().removeClass('has-error');
    $('#txtPassword').next().text('');
    $('#txtConfirmPassword').parent().removeClass('has-error');
    var isValid = true;
    if(!$("#checkbox1").prop('checked')){
        $('#checkbox1').addClass('disabled');
        $('#btnRegistration').text('请先同意协议');
        $('#btnRegistration').addClass('disabled');
    } else if ($('#txtUserName').val() == '') {
        $('#btnRegistration').text('请输入用户名！');
        $('#btnRegistration').addClass('disabled');
        $('#txtUserName').parent().addClass('has-error');
        isValid = false;
    } else if ($('#txtPassword').val() == '') {
        $('#btnRegistration').text('请输入密码！');
        $('#btnRegistration').addClass('disabled');
        $('#txtPassword').parent().addClass('has-error');
        isValid = false;
    } else if ($('#txtConfirmPassword').val() == '') {
        $('#btnRegistration').text('请输入确认密码！');
        $('#btnRegistration').addClass('disabled');
        $('#txtConfirmPassword').parent().addClass('has-error');
        isValid = false;
    } else if ($('#txtPassword').val() != $('#txtConfirmPassword').val()) {
        $('#btnRegistration').text('两次输入的密码不一致！');
        $('#btnRegistration').addClass('disabled');
        $('#txtConfirmPassword').parent().addClass('has-error');
        isValid = false;
    }
    if(isValid&&$("#checkbox1").prop('checked')){
        validatePass=true;
    }
    return validatePass;
}

RegistrationController.prototype.registration = function () {
    var self = this;
    var timestamp=new Date().getTime();
    $('#btnRegistration').addClass('disabled');
    $('#btnRegistration').text('注册中...');
    Site.callAPI(AppConfig.apiURL+'register', 'POST', {
        login_name: $('#txtUserName').val(),
        password: $('#txtPassword').val(),
        time:timestamp,
        key:$.md5("a0f33a00f12b4a8eb6f626f03a1f140a"+timestamp)
    }, function (resp) {
        //$('#btnRegistration').removeClass('disabled');
        //$('#btnRegistration').text('注册');
        // localStorage.setItem('__SID__', data.SID);
        // self.loadingUserInfo();
        if(resp.result=="success"){
            // var exp = new Date();
            // // exp.setTime(exp.getTime() + 3 * 24 * 60 * 60 * 1000);
            // document.cookie = "id="+resp.data.id+",token=" + resp.data.token + ";path=/";
            window.location.href=AppConfig.localURL+"public/login.html";
        }else if(resp.result=="error"){
            alert("用户名或密码错误，登录失败！")
        }
    }, function (error) {
        $('#btnRegistration').removeClass('disabled');
        $('#btnRegistration').text('注册');
        $('#statusView').text(error);
        $('#statusView').show();
    });
}
// LoginController.prototype.loadingUserInfo = function () {
//     //Loading login user info
//     Site.callAPI(/*Site.apiURL + 'auth/getLoginInfo'*/'http://112.74.52.72:8080/OurNews/login', 'GET', {"__SID__": localStorage.getItem('__SID__')}, function (data) {
//         $('#btnRegistration').removeClass('disabled');
//         $('#btnRegistration').text('注册');
//         if (data.permission.admin) {
//             window.location.href = 'admin/client.php';
//         } else if (data.permission.client) {
//             window.location.href = 'client/index.html';
//         } else {
//             alert('Your account has problem, please contact administrator');
//         }
//
//     }, function (error, result) {
//         $('#btnRegistration').removeClass('disabled');
//         $('#btnRegistration').text('注册');
//         //Has not login
//         alert(error);
//     });
// }
$("#txtUserName").bind('input propertychange', function () {
    if ($('#txtUserName').val() == '') {
        $('#btnRegistration').text('请输入用户名！');
        $('#btnRegistration').addClass('disabled');
        $('#txtUserName').parent().addClass('has-error');
    } else if ($('#txtPassword').val() == '') {
        $('#btnRegistration').text('请输入密码！');
        $('#btnRegistration').addClass('disabled');
        $('#txtPassword').parent().addClass('has-error');
    } else if ($('#txtConfirmPassword').val() == '') {
        $('#btnRegistration').text('请输入确认密码！');
        $('#btnRegistration').addClass('disabled');
        $('#txtPassword').parent().addClass('has-error');
    } else if ($("#checkbox1").prop('checked') == '') {
        $('#btnRegistration').text('请先同意协议！');
        $('#btnRegistration').addClass('disabled');
        $('#checkbox1').parent().parent().addClass('has-error');
    } else{
        $('#txtConfirmPassword').next().next().text('注册');
        $('#btnRegistration').removeClass('disabled');
    }
});

$("#txtPassword").bind('input propertychange', function () {
    if ($('#txtUserName').val() == '') {
        $('#btnRegistration').text('请输入用户名！');
        $('#btnRegistration').addClass('disabled');
        $('#txtUserName').parent().addClass('has-error');
    } else if ($('#txtPassword').val() == '') {
        $('#btnRegistration').text('请输入密码！');
        $('#btnRegistration').addClass('disabled');
        $('#txtPassword').parent().addClass('has-error');
    } else if ($('#txtConfirmPassword').val() == '') {
        $('#btnRegistration').text('请输入确认密码！');
        $('#btnRegistration').addClass('disabled');
        $('#txtPassword').parent().addClass('has-error');
    } else if ($("#checkbox1").prop('checked') == '') {
        $('#btnRegistration').text('请先同意协议！');
        $('#btnRegistration').addClass('disabled');
        $('#checkbox1').parent().parent().addClass('has-error');
    } else{
        $('#txtConfirmPassword').next().next().text('注册');
        $('#btnRegistration').removeClass('disabled');
    }
});
$("#txtConfirmPassword").bind('input propertychange', function () {
    if ($('#txtUserName').val() == '') {
        $('#btnRegistration').text('请输入用户名！');
        $('#btnRegistration').addClass('disabled');
        $('#txtUserName').parent().addClass('has-error');
    } else if ($('#txtPassword').val() == '') {
        $('#btnRegistration').text('请输入密码！');
        $('#btnRegistration').addClass('disabled');
        $('#txtPassword').parent().addClass('has-error');
    } else if ($('#txtConfirmPassword').val() == '') {
        $('#btnRegistration').text('请输入确认密码！');
        $('#btnRegistration').addClass('disabled');
        $('#txtPassword').parent().addClass('has-error');
    } else if ($("#checkbox1").prop('checked') == '') {
        $('#btnRegistration').text('请先同意协议！');
        $('#btnRegistration').addClass('disabled');
        $('#checkbox1').parent().parent().addClass('has-error');
    } else{
        $('#txtConfirmPassword').next().next().text('注册');
        $('#btnRegistration').removeClass('disabled');
    }
});
$('#checkbox1').change(function () {
    if($("#checkbox1").prop('checked')){
        $("#checkbox1").parent().next().text('注册');
        $('#btnRegistration').removeClass('disabled');
    }else {
        $("#checkbox1").parent().next().text('请先同意协议');
        $('#btnRegistration').addClass('disabled');
    }
})
