package cn.pzhu.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 专业实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Major implements Serializable {

    private static final long serialVersionUID = -8361409901699926591L;

    private Integer id;
    private String name;
    private Integer schoolId;


}
