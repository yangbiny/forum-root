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
            "<button class='btn btn-sm' data-toggle='modal' onclick='selectIntegralItem("+da.id+",0)'>查看积分明细" +
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

function selectIntegralItem(integralId,start) {
    console.log("开始查询")
    $.ajax({
        url: "/admin/integral/user/list/?start=" + start+"&integralId="+integralId,
        type: "GET",
        success: function (response) {
            if(response.status !== 200){
                alert(response.message)
            }
            fillIntegralItem(integralId,response);
        }
    });
}

function fillIntegralItem(integralId,response) {
    $("#myModal").modal('show')
    $("#integralItem").empty();
    var index = 0;
    var html = "";
    data = response.data;
    for (let dataKey in data){
        index += 1
        da = data[dataKey]
        html += "<tr>" +
            "<th>"+index+"</th>" +
            "<td>" +da.userId+"</td>" +
            "<td>" +da.integralType+"</td>" +
            "<td>"+da.type+"</td>"+
            "<td>"+da.num+"</td>" +
            "<td>"+new Date(da.time)+"</td>" +
            "</tr>";
    }
    console.log(response)
    if(response.hasMore){
        html += "<tr>" +
            "<td colspan='6' align='center'>" +
            "<button class='btn btn-success' onclick='selectIntegralItem("+integralId+","+response.nextStart+")'>下一页</button>" +
            "</td>" +
            "</tr>";
    }
    $("#integralItem").html(html)
}


function selectUserIntegralForAdminList(start) {

    $.ajax({
        url: "/admin/integral/by_search/?userId=" + $("#integralText").val(),
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