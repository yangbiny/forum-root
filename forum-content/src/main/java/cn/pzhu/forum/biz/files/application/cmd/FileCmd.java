package cn.pzhu.forum.biz.files.application.cmd;

import lombok.Data;

/**
 * @author impassivey
 */
@Data
public class FileCmd {

  private String userId;

  private String path;

  private Integer size;

  private String title;

  private String introduction;

  private Integer integral;

}
