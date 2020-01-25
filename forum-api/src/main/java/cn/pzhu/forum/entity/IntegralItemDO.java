package cn.pzhu.forum.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author impassivey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegralItemDO {

  private Integer id;

  private String userId;

  private String integralType;

  private String type;

  private Integer num;

  private Date time;

}
