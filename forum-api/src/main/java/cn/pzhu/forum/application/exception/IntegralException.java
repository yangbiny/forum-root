package cn.pzhu.forum.application.exception;

/**
 * @author impassivey
 */
public class IntegralException extends RuntimeException {

  public IntegralException(String message, Object... para) {
    super(String.format(message, para));
  }
}
