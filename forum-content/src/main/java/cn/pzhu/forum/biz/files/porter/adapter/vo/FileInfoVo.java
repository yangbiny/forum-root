package cn.pzhu.forum.biz.files.porter.adapter.vo;

import java.util.Date;
import lombok.Data;

/**
 * @author impassivey
 */
@Data
public class FileInfoVo {

  private String id;

  private String userId;

  private String path;

  private Date time;

  private Integer size;

  private String title;

  private String introduction;

  private Integer integral;

  private Integer downNum;

}
