var replyToData = "";

function reply() {

    let val = $("#comment").val();
    let id = $("#articleId").val();

    if (val === " " || val === null) {
        alert("请先输入内容!");
    } else {
        $.ajax({
            url: "/user/article/reply",
            type: "post",
            data: {content: val, articleId: id, replyTo: replyToData},
            success: function (data) {
                alert(data.message);
                window.location.reload();
                replyToData = "";

            },
            error: function (data) {
                alert("发生未知错误");
                replyToData = "";
            }
        });
    }

}

function replyTo(data) {


    replyToData = data;
    var id = "#userName-" + data;

    let text = $(id).text();
    let content = "[--Reply To " + text + " --]\n";

    let item = $("#comment");
    item.val(content);
    item.focus();

}

function likeIt() {
    $.ajax({
        url: "/user/article/like",
        type: "post",
        data: {principal: $("#principal").val()},
        success: function (data) {
            window.location.reload();
        },
        error: function (data) {
            alert("发生未知错误");
        }
    });
}