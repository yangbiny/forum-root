package cn.pzhu.forum.utils;

import lombok.Data;

/**
 * @author impassivey
 */
@Data
public class Resp<T> {

  private String message;

  private Integer status;

  private T data;

  public Resp(T data) {
    this.data = data;
    message = "OK";
    status = 200;
  }

  public Resp(T data, Integer status, String message) {
    this.data = data;
    this.status = status;
    this.message = message;
  }


}
