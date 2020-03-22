package cn.pzhu.forum.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
    private Integer status;
    private Sort sort;
}
