function rejectArticle(item) {
    $.ajax({
        url: "/user/article/reject",
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
            console.log(data)
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

function selectArticleForAdmin() {
    var textValue = $("#articleText").val();
    if(textValue === ""){
        alert("搜索的信息不能为空");
    }else {
        $.ajax({
            url: "/admin/select/article/by_search/",
            data: {text: textValue},
            type: "GET",
            success: function (response) {
                if(response.status === 200){
                    fillArticle(response.data)
                }
            },
            error: function (data) {
                alert("发生未知错误!")
            }
        });
    }
}

function fillArticle(data) {
    console.log(data);
    $("#articleList").empty();
    var html = "";
    var index = 1;
    for (let dataKey in data) {
        da = data[dataKey];
        html += "<tr>'" +
            "<th scope='row' style='width: 20px;height:20px;overflow: hidden;'>"+index+"</th>'" +
            "<td>" +
            "<div style='width: 140px;height:20px;overflow: hidden;'>"+da.title+"</div>" +
            "</td>" +
            "<td style='width: 140px;height:20px;overflow: hidden;'>"+da.userName+"</td>" +
            "<td style='width: 40%;height:20px;overflow: hidden;' align='right'>" +
            "<button class='btn btn-sm' data-toggle='modal' onclick=showArticle("+da.id+")>预览</button>" +
            "<div aria-hidden='true' aria-labelledby='myModalLabel' class='modal fade' id='myModal' role='dialog' tabindex='-1'>" +
            "<div class='modal-dialog' role='document' style='width: 1200px'>" +
            "<div class='modal-content'>" +
            "<div class='modal-header'>" +
            "<h2>预览界面</h2>" +
            "<button aria-label='Close' class='close' data-dismiss='modal'" +
            "type='button'><span aria-hidden='true'>&times;</span></button>" +
            "</div>" +
            "<div class='modal-body' id='modal-body'>" +"</div>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "<button class='btn btn-danger small'" +
            "onclick=rejectArticle("+da.id+")>屏蔽" +
            "</button>" +
            "<button class='btn btn-danger small' onclick=setTop("+da.id+")>置顶</button>" +
            "</td>" +
            "</tr>";
    }
    $("#articleList").html(html);
    index = index + 1;
}