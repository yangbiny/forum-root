package cn.pzhu.forum.content;

import lombok.Getter;

/**
 * @program: forum-root
 * @description: 文章的类别(大方向的分类)
 * @author: Impassive
 * @create: 2019-05-27 19:13
 **/
@Getter
public enum ArticleType {


    BACK(1, "后台",9),
    FRONT(2, "前端",10),
    BASIC(3, "基础",11);

    private final Integer type;
    private final String message;

    private final Integer code;

    ArticleType(Integer type, String message,Integer code) {
        this.type = type;
        this.message = message;
        this.code = code;
    }

}
