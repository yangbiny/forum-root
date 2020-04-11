function uploadFiles() {

  $.ajax({
    type: "post",
    url: "/files/upload/files/",
    data: new FormData($("#uploadFilesForm")[0]),
    processData: false,  // 告诉jQuery不要去处理发送的数据
    contentType: false,
    success: function (data) {
      alert(data.msg);
      $("#close").click();
    }

  });
}

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

function updateArticle(item) {
  window.location.href = "/user/article/editor/get?id=" + item;
}

function deleteFiles(item) {

  $.ajax({
    url: "/files/user/" + item + "/",
    type: "delete",
    success: function (data) {
      alert(data.message);
      window.location.reload();
    }
  });

}

function downFiles(item) {

  $.ajax({
    url: "/files/user/down/" + item + "/",
    type: "GET",
    success: function (data) {
      if (data.status === 200) {
        window.location.href = data.data;
      } else {
        alert(data.message);
      }

    }
  });

}

var userId = null;
function selectUserIntegralItem(start) {
  userId = $("#userId").text();
  if (start === undefined){
    start = 0;
  }
  $.ajax({
    url: "/integral/user/list/by_userId/?start=" + start+"&userId="+userId,
    type: "GET",
    success: function (response) {
      if(response.status !== 200){
        alert(response.message)
      }
      fillIntegralItem(userId,response);
    }
  });
}

function fillIntegralItem(userId,response) {
  $("#myIntegralModel").modal('show')
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
        "<button class='btn btn-success' onclick='selectUserIntegralItem("+response.nextStart+")'>下一页</button>" +
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
