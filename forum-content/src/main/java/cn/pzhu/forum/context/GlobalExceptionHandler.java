package cn.pzhu.forum.context;

import cn.pzhu.forum.application.exception.IntegralException;
import cn.pzhu.forum.utils.Resp;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author impassivey
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = IntegralException.class)
  @ResponseBody
  public Resp<String> httpRequestNotSupport(IntegralException exception) {
    String message1 = exception.getMessage();
    return new Resp<>(message1);
  }

}