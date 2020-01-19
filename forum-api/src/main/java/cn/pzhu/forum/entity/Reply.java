package cn.pzhu.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 回复实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Reply implements Serializable {


    private static final long serialVersionUID = -6446447072708176512L;

    private Integer id;
    private Integer articleId;
    private String userName;
    private String time;
    private String content;

    /**
     * 可以为空。为空时表示为评论信息，不为空时表示为回复信息，指向回复的评论ID
     */
    private Integer replyTo;

    /**
     * 用于保存用户头像
     */
    private String avatar;


}
