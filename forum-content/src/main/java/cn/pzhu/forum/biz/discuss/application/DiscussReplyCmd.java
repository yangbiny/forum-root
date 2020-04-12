package cn.pzhu.forum.biz.discuss.application;

import lombok.Data;

@Data
public class DiscussReplyCmd {

    private Integer discussId;

    private String content;

    private String userId;
}
