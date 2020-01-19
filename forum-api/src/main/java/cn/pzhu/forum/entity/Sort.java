package cn.pzhu.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 分类实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Sort implements Serializable {

    private static final long serialVersionUID = -2482648214502058194L;

    private Integer id;
    private String name;
    private Integer rely;
    private Integer number;

    /**
     * 用于存放该分类下的文章集合
     */
    private List<Article> list;


}
