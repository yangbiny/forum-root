package cn.pzhu.forum.service;

import cn.pzhu.forum.entity.Reply;

import java.util.List;

/**
 * @program: forum-root
 * @description: 博客回复信息服务标准
 * @author: Impassive
 * @create: 2019-05-21 09:07
 **/
public interface ReplyService {

    /**
     * 获得留言集合
     *
     * @param id 文章ID
     * @return 留言集合
     */
    List<Reply> list(Integer id);

    /**
     * 添加留言
     *
     * @param reply 留言内容
     * @return 添加状态
     */
    boolean add(Reply reply);

    /**
     * 统计讨论次数
     *
     * @return 讨论次数
     */
    int replyCount();


}
