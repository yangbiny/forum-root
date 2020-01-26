package cn.pzhu.forum.biz.files.porter.adapter.form;

import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 用户上传文件的时候填入的文件的信息
 *
 * @author impassivey
 */
@Data
public class FileForm implements Serializable {

  private static final long serialVersionUID = -5439996081481849071L;

  @NotEmpty(message = "文件的标题信息不能为空")
  private String title;

  @NotEmpty(message = "文件的描述信息不能为空")
  private String introduction;

  @NotNull
  @Min(value = 0, message = "下载所需积分不能为负数，最小为0")
  @Max(value = 5, message = "下载所需积分不能超过五个")
  private Integer integral;
}
