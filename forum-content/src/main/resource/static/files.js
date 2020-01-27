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
