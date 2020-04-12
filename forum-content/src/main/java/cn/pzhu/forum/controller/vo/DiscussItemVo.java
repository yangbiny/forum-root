package cn.pzhu.forum.controller.vo;

import cn.pzhu.forum.entity.DiscussItemDO;
import cn.pzhu.forum.entity.UserInfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class DiscussItemVo implements Serializable {

    private static final long serialVersionUID = -4554631322037360298L;

    private UserInfo userInfo;

    private DiscussItemDO discussItemDO;

}
