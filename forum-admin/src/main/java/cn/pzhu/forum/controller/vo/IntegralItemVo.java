package cn.pzhu.forum.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class IntegralItemVo implements Serializable {

    private static final long serialVersionUID = 5496552827801465398L;

    private Integer id;

    private String userId;

    private String integralType;

    private String type;

    private Integer num;

    private Date time;
}
