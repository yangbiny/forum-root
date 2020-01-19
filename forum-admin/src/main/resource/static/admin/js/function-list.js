function addSchool() {

    var val = $("#schoolName").val();
    if (val !== "") {
        $.ajax({
            url: "/admin/addSchool",
            type: "post",
            data: {name: val},
            dataType: "json",
            success: function (data) {
                alert(data.msg);
            }
        });
    }
}


function addMajor() {
    var school = $("#schoolId").val();
    var majorName = $("#majorName").val();
    if (school !== "" && majorName !== "") {
        $.ajax({
            url: "/admin/addMajor",
            type: "post",
            data: {schoolId: school, name: majorName},
            dataType: "json",
            success: function (data) {
                alert(data.msg);
            }
        });
    }
}

function changeMajor(item) {

    var id = item.id;
    var items;
    if (id === "updateSchoolId") {
        items = $("#updateMajorId");
    } else {
        items = $("#deleteMajorId");
    }

    var schoolId = item.value;


    $.ajax({
        url: "/admin/list",
        type: "post",
        data: {id: schoolId},
        dataType: "json",
        success: function (data) {
            items.find("option").remove();

            for (var i = 0; i < data.length; i++) {
                items.append("<option value='" + data[i].id + "'>" + data[i].name + "</option>");
            }


        }
    });
}

function updateMajor() {
    var schoolId = $("#updateSchoolId").val();
    var majorId = $("#updateMajorId").val();
    var majorName = $("#updateMajorName").val();

    $.ajax({
        url: "/admin/updateMajor",
        type: "post",
        data: {id: majorId, name: majorName, schoolId: schoolId},
        dataType: "json",
        success: function (data) {
            alert(data.msg);
        }
    });

}

function deleteMajor() {

    var majorId = $("#deleteMajorId").val();

    $.ajax({
        url: "/admin/deleteMajor",
        type: "post",
        data: {id: majorId},
        dataType: "json",
        success: function (data) {
            alert(data.msg);
        },
        error: function () {
            alert("不能删除该专业!");
        }
    });
}

function updateSchool() {

    var schoolId = $("#schoolIdForUpdateSchoolName").val();
    var schoolName = $("#schoolNameForUpdateSchoolName").val();

    if (schoolName !== "") {
        $.ajax({
            url: "/admin/updateSchool",
            type: "post",
            data: {id: schoolId, name: schoolName},
            dataType: "json",
            success: function (data) {
                alert(data.msg);
            },
            error: function (data) {
                alert("错误!");
            }
        });
    } else {
        alert("请输入学院名称!");
    }
}

function deleteSchool() {

    var schoolId = $("#schoolIdForDeleteSchool").val();

    $.ajax({
        url: "/admin/deleteSchool",
        type: "post",
        data: {id: schoolId},
        dataType: "json",
        success: function (data) {
            alert(data.msg);
        },
        error: function (data) {
            alert("不能删除该学院!");
        }
    });
}

function addSort() {
    var name = $("#sortName").val();

    $.ajax({
        url: "/admin/sort/add",
        type: "post",
        data: {name: name},
        dataType: "json",
        success: function (data) {
            alert(data.msg);
            window.location.reload();
        },
        error: function (data) {
            alert("添加失败!");
        }
    });
}

function updateSort() {
    var id = $("#updateSortId").val();
    var name = $("#updateSortName").val();

    $.ajax({
        url: "/admin/sort/update",
        type: "post",
        data: {id: id, name: name},
        dataType: "json",
        success: function (data) {
            alert(data.msg);
            window.location.reload();
        },
        error: function (data) {
            alert("修改失败!");
        }
    });

}

function deleteSort() {
    var id = $("#deleteSort").val();

    $.ajax({
        url: "/admin/sort/delete",
        type: "post",
        data: {id: id},
        dataType: "json",
        success: function (data) {
            alert(data.msg);
            window.location.reload();
        },
        error: function (data) {
            alert("删除失败!");
        }
    });

}

function addSortToSort() {

    let id = $("#updateSortId").val();
    let name = $("#updateSortName").val();

    $.ajax({
        url: "/admin/sort/add",
        type: "post",
        data: {rely: id, name: name},
        dataType: "json",
        success: function (data) {
            alert(data.msg);
            window.location.reload();
        },
        error: function (data) {
            alert("添加失败!");
        }
    });

}