package cn.pzhu.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * 文章实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Article implements Serializable {

    private static final long serialVersionUID = -7324263430976483432L;

    private Integer id;
    private String userId;
    private String title;
    private String userName;
    private String time;
    private Integer sortId;
    private String context;
    private String contextMd;
    private Integer readNumber;
    private Integer top;
    private String principal;

    private Sort sort;
}
