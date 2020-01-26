package cn.pzhu.forum.utils;

import lombok.Data;

/**
 * @author impassivey
 */
@Data
public class Resp<T> {

  private String message;

  private T data;

  public Resp(T data) {
    this.data = data;
    message = "OK";
  }

}
