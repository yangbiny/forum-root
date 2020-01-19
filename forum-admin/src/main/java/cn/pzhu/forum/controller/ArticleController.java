package cn.pzhu.forum.controller;

import cn.pzhu.forum.entity.Article;
import cn.pzhu.forum.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ArticleController {

    @Autowired
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

}
