package cn.pzhu.forum.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户信息实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 8839520695529858494L;

    private String id;
    private String nickName;
    private Integer school;
    private Integer major;
    private String avatar;
    private String phone;
    private String description;
    private Integer integral;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserInfo)) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(getId(), userInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
