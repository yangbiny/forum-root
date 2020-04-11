package cn.pzhu.forum.controller;

import cn.pzhu.forum.content.ArticleType;
import cn.pzhu.forum.entity.Article;
import cn.pzhu.forum.entity.Sort;
import cn.pzhu.forum.entity.UserInfo;
import cn.pzhu.forum.service.ArticleService;
import cn.pzhu.forum.service.ForumInfoStatisticsService;
import cn.pzhu.forum.service.SortService;
import cn.pzhu.forum.service.UserInfoService;

import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户帖子管理
 *
 * @author impassivey
 */
@Controller
public class ContentController {

    @Autowired
    private SortService sortService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserInfoService userInfoService;

    @Resource
    private ForumInfoStatisticsService forumInfoStatisticsService;

    /**
     * 查询所有的文章信息
     *
     * @return 文章列表奥
     */
    @RequestMapping("/list")
    public String list() {
        return "articles-list";
    }

    @RequestMapping("/")
    public String welcome(Model model, HttpSession session) {

        String principal = (String) SecurityUtils.getSubject().getPrincipal();

        UserInfo userInfo = userInfoService.get(principal);
        session.setAttribute("userInfo", userInfo);

        return index(model, session);
    }

    /**
     * 转向首页
     *
     * @param model   数据域
     * @param session 用于存储数据
     * @return 返回页面信息
     */
    @RequestMapping("/index")
    public String index(Model model, HttpSession session) {

        // 获得分类信息的前四个（只查询四个）
        List<Sort> list = sortService.list();
        List<Sort> sorts = new ArrayList<>();

        list.stream().limit(4).forEach((x) -> {

            List<Article> list1 = articleService.list(x.getId(), 4);
            x.setList(list1);

            sorts.add(x);
        });

        model.addAttribute("sorts", sorts);
        model.addAttribute("sortList", list);

        Object principal = SecurityUtils.getSubject().getPrincipal();

        session.setAttribute("user", principal);

        return "index";
    }


    @RequestMapping("/article/list/{category}")
    public String articleList(@PathVariable String category, Model model) {

        if ("basic".equals(category)) {
            List<Sort> list = sortService.list(ArticleType.BASIC);

            List<Sort> sorts = new ArrayList<>();

            list.stream().limit(4).forEach((x) -> {

                List<Article> list1 = articleService.list(x.getId(), 4);
                x.setList(list1);

                sorts.add(x);
            });

            model.addAttribute("sorts", sorts);
            return "elements";
        }

        if ("back".equals(category)) {

            List<Article> list = articleService.list(ArticleType.BACK, 4);
            model.addAttribute("articles", list);

            List<Article> articles = articleService.topList(ArticleType.BACK, 10);
            model.addAttribute("topList", articles);

            return "backstage";
        }

        if ("front".equals(category)) {

            List<Sort> list = sortService.list(ArticleType.FRONT);

            List<Sort> sorts = new ArrayList<>();

            list.stream().limit(4).forEach((x) -> {

                List<Article> list1 = articleService.list(x.getId(), 4);
                x.setList(list1);

                sorts.add(x);
            });

            model.addAttribute("sorts", sorts);

            List<Article> articles = articleService.topList(ArticleType.FRONT, 10);
            model.addAttribute("topList", articles);

            return "front";
        }

        return "index";
    }

    @ResponseBody
    @GetMapping("/charts/count/")
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


