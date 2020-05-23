function addArticle() {
    let body = testEditor.getHTML();
    let sort = $("#sortId").val();
    let title = $("#articleTitle").val();
    if (body === "" || title === "") {
        alert("请完善信息!");
        return false;
    }
    let textareaSavedHTML = testEditor.getTextareaSavedHTML();
    $("#context").val(textareaSavedHTML);
    return true;
}

function updateArticle() {
    let body = testEditor.getHTML();
    let sort = $("#sortId").val();
    let title = $("#articleTitle").val();
    if (body === "" || title === "") {
        alert("请完善信息!");
        return false;
    }
    let textareaSavedHTML = testEditor.getTextareaSavedHTML();
    $("#context").val(textareaSavedHTML);
    return true;
}