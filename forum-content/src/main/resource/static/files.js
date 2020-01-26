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