package cn.pzhu.forum.controller;

import cn.pzhu.forum.content.ArticleType;
import cn.pzhu.forum.entity.Article;
import cn.pzhu.forum.entity.Sort;
import cn.pzhu.forum.entity.UserInfo;
import cn.pzhu.forum.service.ArticleService;
import cn.pzhu.forum.service.SortService;
import cn.pzhu.forum.service.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户帖子管理
 */
@Controller
public class ContentController {

    @Autowired
    private SortService sortService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserInfoService userInfoService;

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
            List<Sort> list = sortService.list(ArticleType.FRONT);

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


}


