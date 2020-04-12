package cn.pzhu.forum.controller;

import cn.pzhu.forum.biz.discuss.application.DiscussConvert;
import cn.pzhu.forum.biz.discuss.service.DiscussService;
import cn.pzhu.forum.entity.DiscussDO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * 返回页面信息
 *
 * @author impassive
 */
@Controller
public class TemplateController {

    @Resource
    private DiscussService discussService;

    @GetMapping("/discuss/list/")
    public String discuss(Model model) {
        List<DiscussDO> discussDOList = discussService.listDiscuss(0, 10);
        model.addAttribute("discussList", DiscussConvert.convertToDiscussVoList(discussDOList));
        return "discuss";
    }


}
