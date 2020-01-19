package cn.pzhu.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 学院实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class School implements Serializable {


    private static final long serialVersionUID = 2143423768437839956L;

    private Integer id;
    private String name;


}
