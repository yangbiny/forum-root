function passArticle(item) {
    $.ajax({
        url: "/user/article/pass",
        type: "post",
        data: {id: item},
        success: function (data) {
            alert(data.msg);
            window.location.reload();
        }
    });
}

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


function selectUserForAdmin(start, isUser) {
    console.log(isUser !== null);
    var flag = isUser !== null;
    var textValue = "";
    if (flag) {
        textValue = $("#userInfoText").val();
    } else {
        textValue = $("#userText").val();
    }
    if (textValue === "") {
        alert("搜索的信息不能为空");
    } else {
        $.ajax({
            url: "/admin/select/user/by_search/?start=" + start,
            data: {text: textValue},
            type: "GET",
            success: function (response) {
                console.log(response);
                if (response.status === 200) {
                    fillUser(response, flag)
                }
            },
            error: function (data) {
                alert("发生未知错误!")
            }
        });
    }
}

function fillUser(resp, isUser) {
    var data = resp.data;
    var html = "";
    var index = 0;
    if (isUser) {
        $("#user").empty();
        for (let dataKey in data) {
            da = data[dataKey];
            index = index + 1;
            html += "<tr>" +
                "<th scope='row'>" + index + "</th>\n" +
                "<td>" + da.id + "</td>" +
                "<td>" + da.nickName + "</td>" +
                "<td>" +
                "<button class='btn btn-danger small' onclick='deleteUser('" + da.id + "')>删除" +
                " </button>" +
                "</td>" +
                "</tr>"
        }
        $("#user").html(html);
        if (resp.hasMore) {
            fillNumberOfUser(resp);
        }
    } else {
        $("#userList").empty();
        for (let dataKey in data) {
            da = data[dataKey];
            index = index + 1;
            html += "<tr>" +
                " <th scope='row'>" + index + "</th>" +
                "<td style='padding-left: 10px;'>" +
                "<input name='userEmail' style='padding-left:10px;' value='" + da.id + "' type='checkbox'>" +
                "</td>" +
                "<td>" + da.id + "</td>" +
                "<td>" + da.nickName + "</td>" +
                "</tr>";
        }
        $("#userList").html(html);
        if (resp.hasMore) {
            fillNumberOfUserList(resp);
        }
    }
}

function fillNumberOfUserList(data) {
    var html = "<tr>" +
        "<td colspan='4'>" +
        "<nav aria-label='Page navigation example'>" +
        "<ul class='pagination justify-content-center' id='numberOfArticle'>" +
        "<li class='page-item'>" +
        "<a class='page-link' onclick='selectUserForAdmin(" + data.nextStart + ",null)'>下一页</a>" +
        "</li>" +
        "</ul>" +
        "</nav>" +
        "</td>" +
        "</tr>";
    console.log(html);
    $("#userList").append(html);
}

function fillNumberOfUser(data) {
    var html = "<tr>" +
        "<td colspan='4'>" +
        "<nav aria-label='Page navigation example'>" +
        "<ul class='pagination justify-content-center' id='numberOfArticle'>" +
        "<li class='page-item'>" +
        "<a class='page-link' onclick='selectUserForAdmin(" + data.nextStart + ",user)'>下一页</a>" +
        "</li>" +
        "</ul>" +
        "</nav>" +
        "</td>" +
        "</tr>";
    console.log(html);
    $("#user").append(html);
}

function selectArticleForAdmin(start) {
    var textValue = $("#articleText").val();
    if (textValue === "") {
        alert("搜索的信息不能为空");
    } else {
        $.ajax({
            url: "/admin/select/article/by_search/pending/?start=" + start,
            data: {text: textValue},
            type: "GET",
            success: function (response) {
                console.log(response);
                if (response.status === 200) {
                    fillArticle(response)
                }
            },
            error: function (data) {
                alert("发生未知错误!")
            }
        });
    }
}

function selectArticleForAdminList(start) {
    var textValue = $("#articleText").val();
    if (textValue === "") {
        alert("搜索的信息不能为空");
    } else {
        $.ajax({
            url: "/admin/select/article/by_search/?start=" + start+"&limit=10",
            data: {text: textValue},
            type: "GET",
            success: function (response) {
                console.log(response);
                if (response.status === 200) {
                    fillArticleAdmin(response)
                }
            },
            error: function (data) {
                alert("发生未知错误!")
            }
        });
    }
}

function fillArticle(resp) {
    data = resp.data
    $("#articleList").empty();
    $("#numberOfArticle").empty();
    var html = "";
    var index = 1;
    for (let dataKey in data) {
        da = data[dataKey];
        html += "<tr>'" +
            "<th scope='row' style='width: 20px;height:20px;overflow: hidden;'>" + index + "</th>'" +
            "<td>" +
            "<div style='width: 200px;height:20px;overflow: hidden;'>" + da.title + "</div>" +
            "</td>" +
            "<td style='width: 140px;height:20px;overflow: hidden;'>" + da.userName + "</td>" +
            "<td style='width: 40%;height:20px;overflow: hidden;' align='right'>" +
            "<button class='btn btn-sm' data-toggle='modal' onclick=showArticle(" + da.id + ")>预览</button>" +
            "<div aria-hidden='true' aria-labelledby='myModalLabel' class='modal fade' id='myModal' role='dialog' tabindex='-1' align='left'>" +
            "<div class='modal-dialog' role='document' style='width: 1200px'>" +
            "<div class='modal-content'>" +
            "<div class='modal-header'>" +
            "<h2>预览界面</h2>" +
            "<button aria-label='Close' class='close' data-dismiss='modal'" +
            "type='button'><span aria-hidden='true'>&times;</span></button>" +
            "</div>" +
            "<div class='modal-body' id='modal-body'>" + "</div>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "<button class='btn btn-danger small'" +
            "onclick=rejectArticle(" + da.id + ")>屏蔽" +
            "</button>" +
            "<button class='btn btn-danger small' onclick=passArticle(" + da.id + ")>" +
            "通过" +
            "</button>"+
            "<button class='btn btn-danger small' onclick=setTop(" + da.id + ")>置顶</button>" +
            "</td>" +
            "</tr>";
        index = index + 1;
    }
    $("#articleList").html(html);
    console.log(resp.hasMore);
    if (resp.hasMore) {
        fillNumberOfArticle(resp);
    }
}

function fillArticleAdmin(resp) {
    data = resp.data
    $("#articleList").empty();
    $("#numberOfArticle").empty();
    var html = "";
    var index = 1;
    for (let dataKey in data) {
        da = data[dataKey];
        html += "<tr>'" +
            "<th scope='row' style='width: 20px;height:20px;overflow: hidden;'>" + index + "</th>'" +
            "<td>" +
            "<div style='width: 200px;height:20px;overflow: hidden;'>" + da.title + "</div>" +
            "</td>" +
            "<td style='width: 140px;height:20px;overflow: hidden;'>" + da.userName + "</td>" +
            "<td style='width: 40%;height:20px;overflow: hidden;' align='right'>" +
            "<button class='btn btn-sm' data-toggle='modal' onclick=showArticle(" + da.id + ")>预览</button>" +
            "<div aria-hidden='true' aria-labelledby='myModalLabel' class='modal fade' id='myModal' role='dialog' tabindex='-1' align='left'>" +
            "<div class='modal-dialog' role='document' style='width: 1200px'>" +
            "<div class='modal-content'>" +
            "<div class='modal-header'>" +
            "<h2>预览界面</h2>" +
            "<button aria-label='Close' class='close' data-dismiss='modal'" +
            "type='button'><span aria-hidden='true'>&times;</span></button>" +
            "</div>" +
            "<div class='modal-body' id='modal-body'>" + "</div>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "<button class='btn btn-danger small'" +
            "onclick=rejectArticle(" + da.id + ")>屏蔽" +
            "</button>" +
            "<button class='btn btn-danger small' onclick=setTop(" + da.id + ")>置顶</button>" +
            "</td>" +
            "</tr>";
        index = index + 1;
    }
    $("#articleList").html(html);
    console.log(resp.hasMore);
    if (resp.hasMore) {
        fillNumberOfArticleAdmin(resp);
    }
}

function fillNumberOfArticle(data) {
    var html = "<tr>" +
        "<td colspan='4'>" +
        "<nav aria-label='Page navigation example'>" +
        "<ul class='pagination justify-content-center' id='numberOfArticle'>" +
        "<li class='page-item'>" +
        "<a class='page-link' onclick='selectArticleForAdmin(" + data.nextStart + ")'>下一页</a>" +
        "</li>" +
        "</ul>" +
        "</nav>" +
        "</td>" +
        "</tr>";
    console.log(html);
    $("#articleList").append(html);
}

function fillNumberOfArticleAdmin(data) {
    var html = "<tr>" +
        "<td colspan='4'>" +
        "<nav aria-label='Page navigation example'>" +
        "<ul class='pagination justify-content-center' id='numberOfArticle'>" +
        "<li class='page-item'>" +
        "<a class='page-link' onclick='selectArticleForAdmin(" + data.nextStart + ")'>下一页</a>" +
        "</li>" +
        "</ul>" +
        "</nav>" +
        "</td>" +
        "</tr>";
    console.log(html);
    $("#articleList").append(html);
}


function showArticleList(start) {
    $.ajax({
        url: "/admin/article/list/search/?start=" + start,
        type: "GET",
        success: function (response) {
            console.log(response);
            if (response.status === 200) {
                fillArticleList(response)
            }
        },
        error: function (data) {
            alert("发生未知错误!")
        }
    });
}

function fillArticleList(resp) {
    data = resp.data
    $("#articleList").empty();
    $("#numberOfArticle").empty();
    var html = "";
    var index = 1;
    for (let dataKey in data) {
        da = data[dataKey];
        html += "<tr>'" +
            "<th scope='row' style='width: 20px;height:20px;overflow: hidden;'>" + index + "</th>'" +
            "<td>" +
            "<div style='width: 200px;height:20px;overflow: hidden;'>" + da.title + "</div>" +
            "</td>" +
            "<td style='width: 140px;height:20px;overflow: hidden;'>" + da.userName + "</td>" +
            "<td style='width: 40%;height:20px;overflow: hidden;' align='right'>" +
            "<button class='btn btn-sm' data-toggle='modal' onclick=showArticle(" + da.id + ")>预览</button>" +
            "<div aria-hidden='true' aria-labelledby='myModalLabel' class='modal fade' id='myModal' role='dialog' tabindex='-1' align='left'>" +
            "<div class='modal-dialog' role='document' style='width: 1200px'>" +
            "<div class='modal-content'>" +
            "<div class='modal-header'>" +
            "<h2>预览界面</h2>" +
            "<button aria-label='Close' class='close' data-dismiss='modal'" +
            "type='button'><span aria-hidden='true'>&times;</span></button>" +
            "</div>" +
            "<div class='modal-body' id='modal-body'>" + "</div>" +
            "</div>" +
            "</div>" +
            "</div>" + getStatus(da.status) + getIsTop(da.top) +
            "</td>" +
            "</tr>";
        index = index + 1;
    }
    $("#articleList").html(html);
    console.log(resp.hasMore);
    if (resp.hasMore) {
        fillNumberArticleList(resp);
    }
}

function fillNumberArticleList(data) {
    var html = "<tr>" +
        "<td colspan='4'>" +
        "<nav aria-label='Page navigation example'>" +
        "<ul class='pagination justify-content-center' id='numberOfArticle'>" +
        "<li class='page-item'>" +
        "<a class='page-link' onclick='showArticleList(" + data.nextStart + ")'>下一页</a>" +
        "</li>" +
        "</ul>" +
        "</nav>" +
        "</td>" +
        "</tr>";
    console.log(html);
    $("#articleList").append(html);
}

function getStatus(status) {
    if (status === 0) {
        return "<button class='btn btn-danger small'>待审核</button>";
    } else if (status === -1) {
        return "<button class='btn btn-danger small'>审核未通过</button>";
    } else if (status === 1) {
        return "<button class='btn btn-danger small'>正常</button>";
    } else {
        return "<button class='btn btn-danger small'>已删除</button>";
    }
}

function getIsTop(top) {
    console.log("top : "+top);
    if (top === 1) {
        return "<button class='btn btn-danger small'>已置顶</button>";
    } else {
        return "<button class='btn btn-danger small'>未置顶</button>";
    }
}