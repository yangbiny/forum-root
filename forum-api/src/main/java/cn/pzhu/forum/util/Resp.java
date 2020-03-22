package cn.pzhu.forum.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author impassivey
 */
@Data
@AllArgsConstructor
public class Resp<T> {

  private Integer status;

  private String message;

  private T data;

  public Resp() {
    this.status = RespStatus.SUCCESS.code;
    this.message = RespStatus.SUCCESS.message;
  }

  public Resp(T data) {
    this.status = RespStatus.SUCCESS.code;
    this.message = RespStatus.SUCCESS.message;
    this.data = data;
  }

  @Getter
  public enum RespStatus {

    /**
     * 内部错误
     */
    INTERNAL_ERROR(500, "内部错误"),

    /**
     * 成功
     */
    SUCCESS(200, "成功");

    private Integer code;

    private String message;

    RespStatus(Integer code, String message) {
      this.code = code;
      this.message = message;
    }
  }

}