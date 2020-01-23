package cn.pzhu.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户实体
 *
 * @author yangb
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 8784531370301794401L;

    private String id;
    private String userId;
    private String qq;
    private String password;
    private Integer status;
    private Integer identify;


}
