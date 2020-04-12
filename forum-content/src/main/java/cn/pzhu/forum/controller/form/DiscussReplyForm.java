package cn.pzhu.forum.controller.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class DiscussReplyForm implements Serializable {

    private static final long serialVersionUID = -239572605599627932L;

    @NotNull(message = "讨论的ID不能为空")
    private Integer discussId;

    @NotEmpty(message = "回复的信息不能为空")
    private String content;

}
