package cn.pzhu.forum.controller;

import cn.pzhu.forum.content.ArticleStatus;
import cn.pzhu.forum.content.TopFlag;
import cn.pzhu.forum.content.URLContent;
import cn.pzhu.forum.entity.Article;
import cn.pzhu.forum.entity.Major;
import cn.pzhu.forum.entity.School;
import cn.pzhu.forum.entity.Sort;
import cn.pzhu.forum.entity.UserInfo;
import cn.pzhu.forum.service.ArticleService;
import cn.pzhu.forum.service.MajorService;
import cn.pzhu.forum.service.ReplyService;
import cn.pzhu.forum.service.SchoolService;
import cn.pzhu.forum.service.SortService;
import cn.pzhu.forum.service.UserInfoService;
import cn.pzhu.forum.service.UserService;
import cn.pzhu.forum.util.Resp;
import cn.pzhu.forum.util.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员页面主控制器，用于进入不同的信息模块，进入该模块需要admin角色信息，否则会被拦截到登录界面
 */
@Controller
@Slf4j
@RequestMapping("/admin")
@RequiresRoles("admin")
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * 提供用户信息管理服务
     */
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 提供学院信息管理维护
     */
    @Autowired
    private SchoolService schoolService;

    /**
     * 提供专业管理维护
     */
    @Autowired
    private MajorService majorService;

    /**
     * 提供分类信息管理维护
     */
    @Autowired
    private SortService sortService;

    /**
     * 提供文章信息相关服务
     */
    @Autowired
    private ArticleService articleService;

    /**
     * 提供讨论信息管理服务
     */
    @Autowired
    private ReplyService replyService;

    /**
     * 进入管理员首页，获得登录角色信息
     *
     * @param session  session域，用于存放用户信息
     * @param response 用于控制页面跳转
     * @param model    存储数据
     * @return 返回管理员首页页面
     */
    @RequestMapping("/index")
    public String index(HttpSession session, HttpServletResponse response, Model model) {

        // 获得用户信息
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();

        try {
            if (principal == null) {
                response.sendRedirect(URLContent.LOGIN);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert principal != null;
        UserInfo userInfo = userInfoService.get(principal.toString());
        userInfo.setAvatar(userInfo.getAvatar());
        session.setAttribute("userInfo", userInfo);

        // 统计用户总数
        int userCount = userInfoService.userCount();
        model.addAttribute("userCount", userCount);

        //统计文章数量
        int articleCount = articleService.articleCount();
        model.addAttribute("articleCount", articleCount);

        // 统计讨论次数
        int replyCount = replyService.replyCount();
        model.addAttribute("replyCount", replyCount);

        // 点赞次数统计
        int recordCount = articleService.recordCount();
        model.addAttribute("recordCount", recordCount);

        return "index";
    }

    /**
     * 查询数据信息，并反馈到管理员数据管理页面
     *
     * @param session 用于存放数据
     * @return 返回页面信息
     */
    @RequestMapping("/tables")
    public String tables(HttpSession session) {

        log.info("管理员表格信息查询");

        // 查询用户信息
        List<UserInfo> userInfos = userInfoService.list();
        int co = userInfos.size() % 5 == 0 ? userInfos.size() / 5 : (userInfos.size() / 5) + 1;

        session.setAttribute("users", userInfos);
        session.setAttribute("userPages", co);
        session.setAttribute("first", userInfos);
        session.setAttribute("last", co == 1 ? userInfos : null);

        // 查询博客信息
        List<Article> articleList = articleService.listWithPageForAdmin(0,5);
        if (CollectionUtils.isNotEmpty(articleList)) {
            articleList = Optional.of(articleList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(article -> ArticleStatus.PENDING.getCode().equals(article.getStatus()))
                .collect(Collectors.toList());

        }
        co = (int) articleService.list().stream()
                .filter(article -> ArticleStatus.PENDING.getCode().equals(article.getStatus()))
                .count();
        co = co % 5 == 0 ? co / 5 : (co / 5) + 1;

        session.setAttribute("articles", articleList);
        session.setAttribute("articlePage", co);
        session.setAttribute("firstPage", true);
        session.setAttribute("lastPage", co == 1 ? true : null);

        return "tables";
    }

    /**
     * 分析数据，返回到管理员数据分析页面
     *
     * @return 管理员数据分析页面
     */
    @RequestMapping("/charts")
    public String charts() {
        return "charts";
    }

    /**
     * 进入后台表单管理页面，查询学院，专业、分类信息
     *
     * @param model 数据存储模块
     * @return 管理员表单页面
     */
    @RequestMapping("/forms")
    public String forms(Model model) {

        List<School> list = schoolService.list();
        model.addAttribute("schools", list);

        if (list != null && list.size() > 0) {
            int id = list.get(0).getId();

            List<Major> majors = majorService.list(id);
            model.addAttribute("majors", majors);
        }


        List<Sort> sorts = sortService.list();
        model.addAttribute("sorts", sorts);

        return "forms";
    }

    @RequestMapping("/user/delete")
    @ResponseBody
    public Map<String, String> delete(String id) {
        Map<String, String> map = new HashMap<>();
        boolean delete = userService.delete(id);

        if (delete) {
            map.put("msg", "成功!");
        } else {
            map.put("msg", "失败!");
        }
        return map;
    }

    /**
     * 分页（均为每页显示五条数据）
     *
     * @param category 分页种类
     * @param page     查看第几页
     * @param session  session存储域
     * @return 数据显示页面
     */
    @RequestMapping("/page/{category}")
    public String subPage(@PathVariable("category") String category, int page, HttpSession session) {

        if (category.equals("userInfo")) {
            // 查询用户信息
            List<UserInfo> userInfos = userInfoService.list();
            List<UserInfo> list = new ArrayList<>();

            userInfos.stream().skip((page - 1) * 5).limit(5).forEach(list::add);

            int co = userInfos.size() % 5 == 0 ?
                    userInfos.size() / 5 : (userInfos.size() / 5) + 1;

            session.setAttribute("users", list);
            session.setAttribute("userPages", co);
            session.setAttribute("first", page == 1 ? userInfos : null);
            session.setAttribute("last", page == co ? userInfos : null);


        } else {
            int start = (page - 1) * 5;
            int limit = 5;
            List<Article> articleList = articleService.listWithPageForAdmin(start, limit);
            // 文章审核
            if (category.equals("verify")) {
                // 查询博客信息
                int size = articleService.selectPendingArticle();
                int co = size % 5 == 0 ? size / 5 : (size / 5) + 1;
                session.setAttribute("articles", articleList);
                session.setAttribute("articlePage", co);
                session.setAttribute("firstPage", page == 1 ? true : null);
                session.setAttribute("lastPage", page == co ? true : null);
            }
            // 未置顶博客
            if (category.equals("topArticle")) {
                // 查询未置顶博客信息
                List<Article> lists = new ArrayList<>();
                // 选择未置顶的博客信息
                articleList.stream()
                        .filter((x) -> TopFlag.NORMAL.getFlag().equals(x.getTop()))
                        .forEach(lists::add);

                List<Article> articles = new ArrayList<>();

                lists.stream().skip((page - 1) * 5)
                        .limit(5)
                        .forEach(articles::add);

                int co = lists.size() % 5 == 0 ? lists.size() / 5 : (lists.size() / 5) + 1;
                session.setAttribute("topArticles", articles);
                session.setAttribute("topArticlePage", co);
                session.setAttribute("topFirstPage", page == 1 ? true : null);
                session.setAttribute("topLastPage", page == co ? true : null);
            }

        }

        return "tables";
    }

    @RequestMapping("/notice/users")
    @ResponseBody
    public Map<String, String> notice(String users, String message) {
        Map<String, String> map = new HashMap<>();
        String[] user = users.split(",");
        boolean flag = Utils.sendBatchMail(message, user, "通知信息");
        if (flag) {
            map.put("msg", "成功!");
        } else {
            flag = Utils.sendBatchMail(message, user, "通知信息");

            if (flag) {
                map.put("msg", "成功!");
            } else {
                map.put("msg", "错误");
            }
        }
        return map;
    }

    @ResponseBody
    @GetMapping("/select/user/by_search/")
    public Resp<List<UserInfo>> selectUserBySearchForAdmin(
            @RequestParam String text,
            @RequestParam(required = false,defaultValue = "0")Integer start,
            @RequestParam(required = false,defaultValue = "1")Integer limit
    ){
        // 查询用户信息
        List<UserInfo> userInfos = userInfoService.selectUserBySearch(text,start,limit+1);
        Resp<List<UserInfo>> resp = new Resp<>();
        if(CollectionUtils.size(userInfos) > limit){
            resp.setHasMore(true);
            userInfos.remove(userInfos.size()-1);
            resp.setNextStart(start + limit);
        }
        resp.setData(userInfos);
        return resp;
    }
}
