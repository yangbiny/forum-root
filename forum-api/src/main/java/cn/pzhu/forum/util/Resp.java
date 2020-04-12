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

  private Boolean hasMore = false;

  private Integer nextStart = 0;

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

  public Resp(RespStatus status,String message) {
    this.status = status.code;
    this.message = message;
  }

  public Resp(RespStatus status) {
    this.status = status.code;
    this.message = status.message;
    this.data = null;
  }

  public Resp(Boolean hasMore,Integer nextStart,T data) {
    this.status = RespStatus.SUCCESS.code;
    this.message = RespStatus.SUCCESS.message;
    this.data = data;
    this.hasMore = hasMore;
    this.nextStart = nextStart;
  }

  @Getter
  public enum RespStatus {

    /**
     * 内部错误
     */
    INTERNAL_ERROR(500, "内部错误"),

    /**
     * 内部错误
     */
    ILLEGAL_PARAM(400, "参数错误"),

    /**
     * 成功
     */
    SUCCESS(200, "成功");

    private final Integer code;

    private final String message;

    RespStatus(Integer code, String message) {
      this.code = code;
      this.message = message;
    }
  }

}