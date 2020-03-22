package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.dao.ArticleDao;
import cn.pzhu.forum.dao.ReplyDao;
import cn.pzhu.forum.dao.UserInfoDao;
import cn.pzhu.forum.entity.Article;
import cn.pzhu.forum.entity.Reply;
import cn.pzhu.forum.entity.Sort;
import cn.pzhu.forum.entity.UserInfo;
import cn.pzhu.forum.service.ArticleService;
import cn.pzhu.forum.service.ForumInfoStatisticsService;
import cn.pzhu.forum.service.SortService;
import cn.pzhu.forum.util.Utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: forum-root
 * @description: 论坛信息统计实现类
 * @author: Impassive
 * @create: 2019-06-13 10:40
 **/
@Service
@Slf4j
public class ForumInfoStatisticsServiceImpl implements ForumInfoStatisticsService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private SortService sortService;

    @Autowired
    private ReplyDao replyDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public List<Integer> timeCount(boolean flag) {
        // 一年只有12个月
        int[] arr = new int[12];

        List<Article> articles = articleService.list();
        if (flag) {
            System.out.println("待完成");
        } else {

            String year = Utils.getYear();

            articles.stream().filter(x -> !x.getTime().matches(year)).forEach(
                    x -> {
                        String mon = x.getTime().substring(5, 6);
                        int mons = Integer.parseInt(mon);
                        arr[--mons]++;
                    }
            );

            return Arrays.stream(arr).boxed().collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public Map<String, Integer> sortCount() {

        Map<String, Integer> map = new HashMap<>();

        List<Sort> sorts = sortService.list();
        List<Article> articles = articleService.list();

        for (Sort sort : sorts) {
            int count = 0;
            for (Article article : articles) {
                if (sort.getId().equals(article.getSort().getId())) {
                    count++;
                }
            }
            map.put(sort.getName(), count);
        }

        return map;
    }

    @Override
    public Map<String, Integer> maxRecordArticle() {
        Map<String, Integer> map = new HashMap<>();

        List<Article> articles = articleDao.recordList();

        for (Article article : articles) {
            Sort sort = article.getSort();
            map.put(sort.getName(), sort.getId());   // 借助分类ID存储分类出现的次数
        }

        return map;
    }

    @Override
    public Map<String, Integer> maxReplyArticle() {

        Map<String, Integer> map = new HashMap<>();

        List<Reply> list = replyDao.maxReplyCount();

        for (Reply reply : list) {
            map.put(reply.getUserName(), reply.getArticleId()); // 借助博客ID存储讨论次数，用户名存储分类名
        }

        return map;
    }

    @Override
    public Map<String, Integer> registerUserCount() {
        Map<String, Integer> map = new HashMap<>();

        List<UserInfo> list = userInfoDao.registerCount();

        for (UserInfo userInfo : list) {
            map.put(userInfo.getNickName(), userInfo.getSchool()); // 借助学院ID保存注册人数，用户昵称保存学院名字
        }
        return map;
    }
}
