package cn.pzhu.forum.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DiscussDO {

    private Integer id;

    private String title;

    private String content;

    private String userId;

    private Integer integral;

    private Integer status;

    private Date createTime;


}
