function discussReply(discussId) {
    let comment = $("#comment").val();
    var data = {content: comment, discussId: discussId};
    $.ajax({
        url: "/user/discuss/reply/",
        type: "post",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            if(response.status !== 200){
                alert(response.message);
            }
            window.location.reload();
        },
        error: function (data) {
            alert("发生未知错误");
        }
    });
}

function adoption(itemId) {
    console.log(itemId)
    var data = { discussItemId: itemId};
    $.ajax({
        url: "/user/discuss/adoption/",
        type: "post",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            if(response.status !== 200){
                alert(response.message);
            }
            window.location.reload();
        },
        error: function (data) {
            alert("发生未知错误");
        }
    });
}