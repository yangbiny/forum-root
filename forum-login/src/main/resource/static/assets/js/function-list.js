const contentURL = "http://localhost:8002";
const loginURL = "http://localhost:8001";
const adminURL = "http://localhost:8003/admin/index";

function changeMajor(item) {

    var items = $("#updateMajorId");

    var schoolId = item.value;

    $.ajax({
        url: "/list",
        type: "post",
        data: {id: schoolId},
        success: function (data) {
            items.find("option").remove();
            for (var i = 0; i < data.length; i++) {
                items.append("<option value='" + data[i].id + "'>" + data[i].name + "</option>");
            }


        }
    });
}

function toLogin() {
    //以下为按钮点击事件的逻辑。注意这里要重新打开窗口
    //否则后面跳转到QQ登录，授权页面时会直接缩小当前浏览器的窗口，而不是打开新窗口
    //var A=window.open("oauth/index.php","TencentLogin",
    //   "width=450,height=320,menubar=0,scrollbars=1,resizable=1,status=1,titlebar=0,toolbar=0,location=1");

    window.location.href = "qqLogin";
}

function resetPassword() {
    if ($("#resetEmail").val() === "") {
        alert("请输入账号!");
    } else if ($("#resetPassword").val() === "") {
        alert("请输入密码!");
    } else if ($("#confirmPassword").val() !== $("#resetPassword").val()) {
        alert("两次密码不一致!");
    } else if ($("#resetImgCode").val() === "") {
        alert("请输入图片验证码！")
    } else if ($("#resetUserCode").val() === "") {
        alert("请输入邮件验证码!");
    } else {

        $.ajax({
            type: "post",
            url: "resetPassword",
            data: $("#reset").serialize(),
            success: function (data) {
                alert(data.msg);
                if (data.flag === "success") {
                    window.location.reload();
                }
            }
        });
    }
}

function submitForm(e) {
    //e.preventDefault();
    if ($("#nickName").val() === "" && $("#phone").val() === "") {
        alert("请完善信息!");
    } else {
        $.ajax({
            url: "/user/updateInfos",
            type: "post",
            async: false,
            data: $("#userForm").serialize(),
            dataType: "json",
            success: function (data) {
                alert(data.msg);
                window.location.href = contentURL;
            },
            error: function () {
                alert("请登录后重试!");
                window.location.href = loginURL;
            }
        });
    }
}

function userLogin() {

    if ($("#id").val() === "") {
        alert("请输入账号!");
    } else if ($("#password").val() === "") {
        alert("请输入密码!");
    } else if ($("#imgCode").val() === "") {
        alert("请输入验证码!");
    } else {

        $.ajax({
            type: "post",
            url: "/login",
            data: $("#login").serialize(),
            success: function (data) {
                if (data.msg === "admin") {
                    window.location.href = adminURL;
                } else if (data.msg === "user") {
                    window.location.href = contentURL;
                } else {
                    alert(data.msg);
                }

            },
            error: function (data) {
                alert("error:" + data.msg);
            }
        });
    }
}

function changeImg(key) {
    if (key === "reset") {
        $("#resetCode").attr("src", "GenerateVerificationCode?" + Math.random());
    } else {
        $("#code").attr("src", "GenerateVerificationCode?" + Math.random());
    }

}

function changeForm(key) {
    if (key === "create") {
        $("#login").attr("style", "display: none");
        $("#register").attr("style", "display: inline-block");
    }

    if (key === "reset") {
        $("#login").attr("style", "display: none");
        $("#reset").attr("style", "display: inline-block");
    }

}

function registerUser() {

    let codeError = $("#codeError");

    $.ajax({
        type: "post",
        url: "/register",
        data: $("#register").serialize(),
        success: function (data) {
            if (data === "success") {
                alert("注册成功!");
                window.location.href = loginURL;
            } else {
                codeError.text("帐号已存在!");
                codeError.attr("style", "color:red;display: inline-bolock");
            }


        }
    });
}

var countdown = 60;

function send(key) {
    var obj = null;
    var data = null;
    if (key === "reset") {
        obj = $("#resetEmailCode");
        data = $("#resetEmail").val();
    } else {
        obj = $("#emailCode");
        data = $("#email").val();
    }
    if (data === "") {
        alert("请输入邮箱!");
    } else {
        $.ajax({
            url: "generateRegisterCode?email=" + data,
            type: "post",
            success: function (data) {

            },
            error: function () {
                $("#signup-code").val("验证码生成错误");
            }
        });

        setTime(obj);
    }

}

function setTime(obj) { //发送验证码倒计时
    if (countdown === 0) {
        obj.attr('disabled', false);
        obj.val("获取验证码");
        $("#btn").css("background", "#00bc12");
        countdown = 60;
        return;
    } else {
        obj.attr('disabled', true);
        obj.val(countdown + "S后重试");
        countdown--;
        $("#btn").css("background", "#ccc")
    }
    setTimeout(function () {
            setTime(obj)
        }
        , 1000)
}

function checkUserName() {
    let username = $("#username").val();

    $.ajax({
        type: "post",
        url: "/checkUserName",
        data: {username: username},
        success: function (data) {
            let info = $("#info");
            let parentElement = info.parent();

            console.log(info);
            console.log(parentElement);

            if (data.flag === true) {
                info.html("该用户名已经被使用，请更换用户名");
                parentElement.attr("style", "height:30px;margin-bottom: 0");
                info.attr("style", "color:red");
            } else {
                info.html("");
                parentElement.attr("style", "height:0;margin-bottom: 0");
                info.attr("style", "color:black");
            }
        }
    });

}