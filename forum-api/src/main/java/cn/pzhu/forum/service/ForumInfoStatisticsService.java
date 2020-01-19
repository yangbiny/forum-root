package cn.pzhu.forum.service;

import java.util.List;
import java.util.Map;

/**
 * @program: forum-root
 * @description: 论坛信息统计服务模块
 * @author: Impassive
 * @create: 2019-06-13 10:37
 **/
public interface ForumInfoStatisticsService {

    /**
     * 根据时间进行统计
     *
     * @param flag 标志位，true代表统计所有年份，false代表只统计今年
     * @return 月份和对用的博客的数量
     */
    List<Integer> timeCount(boolean flag);

    /**
     * 根据分类统计
     *
     * @return 分类名称和对应的博客数量
     */
    Map<String, Integer> sortCount();

    /**
     * 统计博客点赞较多的分类
     *
     * @return 分类名称和对应的点赞数
     */
    Map<String, Integer> maxRecordArticle();

    /**
     * 统计博客讨论较多的分类
     *
     * @return 分类名称和对应的点赞数
     */
    Map<String, Integer> maxReplyArticle();

    /**
     * 统计每个学院注册学生人数
     *
     * @return 学院学生注册人数
     */
    Map<String, Integer> registerUserCount();

}
