package cn.pzhu.forum.controller;

import cn.pzhu.forum.entity.Article;
import cn.pzhu.forum.entity.Record;
import cn.pzhu.forum.entity.Reply;
import cn.pzhu.forum.entity.UserInfo;
import cn.pzhu.forum.service.ArticleService;
import cn.pzhu.forum.service.ReplyService;
import cn.pzhu.forum.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 查看文章
     *
     * @param user      用户名
     * @param principal 文章主要关键字
     * @param model     数据域
     * @param session   用户存储数据
     * @return 文章查看页面
     */
    @RequestMapping("/article/show/{user}/{principal}")
    public String showArticle(@PathVariable("user") String user, @PathVariable("principal") String principal,
                              Model model, HttpSession session) {

        log.info("cn.pzhu.forum.controller.ArticleController.showArticle-查询文章-入参：user = {},principal = {}",
                user, principal);

        // 查询博客信息
        Article article = articleService.get(user, principal);
        model.addAttribute("article", article);

        // 查询博客的回复信息
        List<Reply> list = replyService.list(article.getId());
        List<List<Reply>> lists = splitList(list);
        model.addAttribute("replys", lists);

        // 统计点赞数
        Integer integer = articleService.likeCount(principal);
        model.addAttribute("like", integer);

        List<Article> similar = articleService.similar(article.getTitle(), 10);
        model.addAttribute("similars", similar);

        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();

        if (authenticated) {
            String userId = (String) SecurityUtils.getSubject().getPrincipal();

            UserInfo userInfo = userInfoService.get(userId);
            boolean flag = articleService.hashLiked(userInfo.getNickName(), principal);

            if (flag) {
                session.setAttribute("recordFlag", new Record());
            } else {
                session.setAttribute("recordFlag", null);
            }
        } else {
            session.setAttribute("recordFlag", null);
        }


        return "single";
    }

    /**
     * 根据关键字查询博客信息（如果没有输入任何的信息，将会查询全部的博客信息）
     *
     * @param key   关键字信息
     * @param model 数据存储区域
     * @return 返回博客显示页面
     */
    @RequestMapping("/article/list/key")
    public String list(String key, Model model) {

        List<Article> list = articleService.list(key.trim());

        model.addAttribute("articles", list);

        return "articles";
    }

    /**
     * 根据指定的分类ID查询该分类下的数据（默认查询10条）
     *
     * @param id    分类ID
     * @param model 数据存储域
     * @return 博客显示页面
     */
    @RequestMapping("/articles/list/sort/{id}")
    public String list(@PathVariable("id") Integer id, Model model) {

        List<Article> list = articleService.list(id, 10);

        model.addAttribute("articles", list);

        return "articles";
    }


    /**
     * 将传进来的所有的留言信息分割为留言信息和回复信息相对应的集合体
     *
     * @param replyList 所有的留言信息
     * @return 分割后的留言信息, 如果留言信息为空，返回null
     */
    private List<List<Reply>> splitList(List<Reply> replyList) {

        if (replyList == null) {
            return null;
        }

        List<List<Reply>> lists = new ArrayList<>();

        for (Reply reply : replyList) {

            // 如果他不为空，说明是二级的，需要在第二个循环里面去添加
            if (reply.getReplyTo() != null) {
                continue;
            }


            List<Reply> list = new ArrayList<>();

            // 为空，添加到一个新的list中
            list.add(reply);

            for (Reply reply1 : replyList) {

                if (reply1.getReplyTo() != null) {

                    if (reply.getId().equals(reply1.getReplyTo())) {
                        list.add(reply1);
                    }

                }

            }

            lists.add(list);
        }

        return lists;
    }


}
