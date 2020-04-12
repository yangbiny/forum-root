package cn.pzhu.forum.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DiscussVo implements Serializable {

    private static final long serialVersionUID = 1979844985134179061L;

    private Integer id;

    private String content;

    private String title;

    private String userId;

    private Integer integral;

    private Integer status;

    private Date createTime;

}
