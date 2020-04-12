package cn.pzhu.forum.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DiscussItemDO {

    private static final long serialVersionUID = -2422263135634401145L;

    private Integer id;

    private Integer discussId;

    private String content;

    private String userId;

    private Integer status;

    private Date createTime;

}
