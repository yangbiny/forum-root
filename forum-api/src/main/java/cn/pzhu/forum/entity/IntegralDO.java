package cn.pzhu.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户总积分的信息
 *
 * @author impassivey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegralDO {

  private Integer id;

  private String userId;

  private Integer num;

}
