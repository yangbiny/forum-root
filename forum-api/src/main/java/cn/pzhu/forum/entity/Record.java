package cn.pzhu.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 帖子点赞
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Record implements Serializable {

    private static final long serialVersionUID = -3069002076850150020L;

    private Integer id;
    private String userName;
    private String articlePrincipal;


}
