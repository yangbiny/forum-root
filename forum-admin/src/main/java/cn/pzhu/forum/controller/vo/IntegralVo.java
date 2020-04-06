package cn.pzhu.forum.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户积分信息
 */
@Data
public class IntegralVo implements Serializable {

    private static final long serialVersionUID = -4081905292795912641L;

    private String userId;

    private Integer number;

}
