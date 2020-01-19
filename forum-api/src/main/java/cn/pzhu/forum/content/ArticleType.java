package cn.pzhu.forum.content;

/**
 * @program: forum-root
 * @description: 文章的类别(大方向的分类)
 * @author: Impassive
 * @create: 2019-05-27 19:13
 **/
public enum ArticleType {


    BACK(1, "后台"),
    FRONT(2, "前端"),
    BASIC(3, "基础");

    private Integer type;
    private String message;

    ArticleType(Integer type, String message) {
        this.type = type;
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
