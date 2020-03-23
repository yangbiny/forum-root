package cn.pzhu.forum.controller;

import cn.pzhu.forum.content.ArticleStatus;
import cn.pzhu.forum.entity.Article;
import cn.pzhu.forum.service.ArticleService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pzhu.forum.util.Resp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @RequestMapping("/user/article/delete")
    public Map<String, String> delete(int id) {

        log.info("cn.pzhu.forum.controller.ArticleController.delete-删除指定的文章-入参:id = {}", id);
        Map<String, String> map = new HashMap<>();
        boolean delete = articleService.delete(id);

        if (delete) {
            map.put("msg", "成功");
        } else {
            map.put("msg", "失败!");
        }

        return map;
    }

    /**
     * 审核被拒绝
     */
    @RequestMapping("/user/article/reject")
    public Map<String, String> reject(int id) {
        log.info("cn.pzhu.forum.controller.ArticleController.reject-删除指定的文章-入参:id = {}", id);
        Map<String, String> map = new HashMap<>();
        boolean reject = articleService.updateArticleStatus(id, ArticleStatus.REJECT);

        if (reject) {
            map.put("msg", "成功");
        } else {
            map.put("msg", "失败!");
        }

        return map;
    }

    /**
     * 审核被拒绝
     */
    @RequestMapping("/user/article/pass")
    public Map<String, String> pass(int id) {
        log.info("cn.pzhu.forum.controller.ArticleController.reject-删除指定的文章-入参:id = {}", id);
        Map<String, String> map = new HashMap<>();
        boolean pass = articleService.updateArticleStatus(id, ArticleStatus.NORMAL);
        if (pass) {
            map.put("msg", "成功");
        } else {
            map.put("msg", "失败!");
        }
        return map;
    }

    @RequestMapping("/user/article/get")
    public String get(int id) {
        log.info("cn.pzhu.forum.controller.ArticleController.get-查询指定的文章的内容（用于预览）-入参:id = {}", id);
        Article article = articleService.get(id);
        if (article != null) {
            return article.getContext();
        }
        return null;
    }

    @RequiresRoles("admin")
    @RequestMapping("/admin/article/setTop")
    public Map<String, String> setTop(int id) {
        log.info("cn.pzhu.forum.controller.ArticleController.setTop-置顶指定的文章-入参:id = {}", id);
        Map<String, String> map = new HashMap<>();
        boolean top = articleService.setTop(id);
        if (top) {
            map.put("msg", "成功");
        } else {
            map.put("msg", "失败!");
        }
        return map;
    }

    @GetMapping("/admin/select/article/by_search/")
    @ResponseBody
    public Resp<List<Article>> selectArticleBySearch(
            @RequestParam String text,
            @RequestParam(required = false,defaultValue = "0") Integer start,
            @RequestParam(required = false,defaultValue = "5") Integer limit
    ){
        List<Article> articles = articleService.selectArticleByKeyword(text,start,limit+1);
       Resp<List<Article>> resp = new Resp<>();
       if(CollectionUtils.size(articles) > limit){
           resp.setHasMore(true);
           articles.remove(articles.size()-1);
           resp.setNextStart(start + limit);
           System.out.println("xx");
       }
       resp.setData(articles);
       return resp;
    }

}
