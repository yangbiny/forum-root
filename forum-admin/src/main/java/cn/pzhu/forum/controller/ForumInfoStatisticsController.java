package cn.pzhu.forum.controller;

import cn.pzhu.forum.service.ForumInfoStatisticsService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @program: forum-root
 * @description: 论坛信息统计，显示在图表中
 * @author: Impassive
 * @create: 2019-06-13 08:36
 **/
@RestController
@RequiresRoles("admin")
@RequestMapping("/admin/forum/count")
public class ForumInfoStatisticsController {

    @Autowired
    private ForumInfoStatisticsService forumInfoStatisticsService;

    /**
     * 根据博客的发表时间统计博客的文章数量（只统计本年份的）
     *
     * @return 时间与数量的映射关系
     */
    @RequestMapping("/home")
    public Map<String, List<?>> article() {
        Map<String, List<?>> map = new HashMap<>();

        // 根据时间统计
        List<Integer> list = forumInfoStatisticsService.timeCount(false);
        map.put("time", list);

        Map<String, Integer> sortCount = forumInfoStatisticsService.sortCount();
        Set<String> strings = sortCount.keySet();

        List<String> key = new ArrayList<>(strings);
        map.put("sortName", key);
        List<Integer> sortVal = new ArrayList<>();

        for (String string : strings) {
            Integer integer = sortCount.get(string);
            sortVal.add(integer);
        }
        map.put("sortCount", sortVal);

        return map;
    }

    @RequestMapping("/charts")
    public Map<String, List<?>> countSort() {
        Map<String, List<?>> map = new HashMap<>();

        // 点赞次数
        Map<String, Integer> maxRecordArticle = forumInfoStatisticsService.maxRecordArticle();

        Set<String> strings = maxRecordArticle.keySet();
        List<String> keySet = new ArrayList<>(strings);
        map.put("sortName", keySet);

        List<Integer> sortVal = new ArrayList<>();

        for (String string : strings) {
            Integer integer = maxRecordArticle.get(string);
            sortVal.add(integer);
        }
        map.put("sortCount", sortVal);

        // 讨论次数
        Map<String, Integer> maxReplyArticle = forumInfoStatisticsService.maxReplyArticle();
        Set<String> keySet1 = maxReplyArticle.keySet();
        List<String> k = new ArrayList<>(keySet1);

        map.put("replySortName", k);

        List<Integer> sortVal1 = new ArrayList<>();

        for (String string : keySet1) {
            Integer integer = maxReplyArticle.get(string);
            sortVal1.add(integer);
        }
        map.put("replySortCount", sortVal1);

        // 统计注册学员
        Map<String, Integer> registerUserCount = forumInfoStatisticsService.registerUserCount();
        Set<String> keySet2 = registerUserCount.keySet();
        List<String> k2 = new ArrayList<>(keySet2);

        map.put("schoolName", k2);

        List<Integer> sortVal2 = new ArrayList<>();

        for (String string : keySet2) {
            Integer integer = registerUserCount.get(string);
            sortVal2.add(integer);
        }
        map.put("userCount", sortVal2);
        return map;
    }

}
