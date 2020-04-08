function queryUserIntegral(nextStart) {
    $.ajax({
        url: "/admin/integral/list/?start=" + nextStart,
        type: "GET",
        success: function (response) {
            if(response.status !== 200){
                alert(response.message)
            }
            console.log(response)
            fillUserIntegral(response)
        }
    });
}

function fillUserIntegral(response) {
    $("#integralList").empty();
    var index = 0;
    var html = ""
    data = response.data
    for (let dataKey in data) {
        index = index + 1
        da = data[dataKey];
        html += "<tr>" +
            "<th scope='row' style='width: 20px;height:20px;overflow: hidden;'>" + index +"</th>" +
            "<td>" +
            "<div style='width: 200px;height:20px;overflow: hidden;'>" +da.userId+
            "</div>" +
            "</td>" +
            "<td style='width: 140px;height:20px;overflow: hidden;'>" +da.number+
            "</td>" +
            "<td style='width: 40%;height:20px;overflow: hidden;' align='right'>" +
            "<button class='btn btn-sm' data-toggle='modal' onclick='selectIntegralItem("+da.userId+",0)'>查看积分明细" +
            "</button>" +
            "</td>" +
            "</tr>";
    }

    if(response.hasMore){
        html += "<tr>" +
            "<td colspan='4'>" +
            "<nav aria-label='Page navigation example'>" +
            "<ul class='pagination justify-content-center'>" +
            "<li class='page-item'>" +
            "<button class='btn btn-default' th:onclick='queryUserIntegral("+response.nextStart+")'>下一页</button>" +
            "</li>" +
            "</ul>" +
            "</nav>" +
            "</td>" +
            "</tr>";
    }
    $("#integralList").html(html);
}

function selectIntegralItem(userId,start) {
    console.log("开始查询")
    $.ajax({
        url: "/admin/integral/user/list/?start=" + start+"&userId="+userId,
        type: "GET",
        success: function (response) {
            if(response.status !== 200){
                alert(response.message)
            }
            console.log(response)
        }
    });


}