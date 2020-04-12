package cn.pzhu.forum.controller.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class DiscussForm implements Serializable {

    private static final long serialVersionUID = 6913296957117114381L;

    @NotEmpty(message = "标题不能为空")
    private String title;

    @NotEmpty(message = "求助内容不能为空")
    private String content;

    @NotNull(message = "积分不能为空")
    private Integer integral;
}
