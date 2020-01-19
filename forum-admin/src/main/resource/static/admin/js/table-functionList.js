function deleteArticle(item) {
    $.ajax({
        url: "/user/article/delete",
        type: "post",
        data: {id: item},
        success: function (data) {
            alert(data.msg);
            window.location.reload();
        }
    });
}

function showArticle(item) {
    $("#modal-body").empty();

    $.ajax({
        url: "/user/article/get",
        data: {id: item},
        type: "post",
        sync: false,
        success: function (data) {

            $("#modal-body").append(data);
        }
    });

    $("#myModal").modal('show')
}

function deleteUser(item) {

    $.ajax({
        url: "/admin/user/delete",
        data: {id: item},
        type: "post",
        success: function (data) {
            alert(data.msg);
            window.location.reload();
        },
        error: function (data) {
            alert("发生未知错误!")
        }
    });

}

function setTop(item) {
    $.ajax({
        url: "/admin/article/setTop",
        data: {id: item},
        type: "post",
        success: function (data) {
            alert(data.msg);
            window.location.reload();
        },
        error: function (data) {
            alert("发生未知错误!")
        }
    });
}

function noticeUser() {
    var arr = [];

    $("input[name='userEmail']:checked").each(function () {
        arr.push($(this).val());
    });

    var val = arr.join(',');

    if (arr.length === 0) {
        alert("请选择用户！");
    } else {
        let s = prompt("请输入需要通知的信息");  // 用户输入的数据

        if (s !== null) {
            $.ajax({
                url: "/admin/notice/users",
                data: {users: val, message: s},
                type: "post",
                success: function (data) {
                    alert(data.msg);
                },
                error: function (data) {
                    alert("发生未知错误!")
                }
            });
        }

    }
}