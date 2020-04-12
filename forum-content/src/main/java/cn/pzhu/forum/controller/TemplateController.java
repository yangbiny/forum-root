package cn.pzhu.forum.controller;

import cn.pzhu.forum.biz.discuss.application.DiscussConvert;
import cn.pzhu.forum.biz.discuss.service.DiscussService;
import cn.pzhu.forum.controller.vo.DiscussItemVo;
import cn.pzhu.forum.entity.DiscussDO;
import cn.pzhu.forum.entity.DiscussItemDO;
import cn.pzhu.forum.entity.UserInfo;
import cn.pzhu.forum.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 返回页面信息
 *
 * @author impassive
 */
@Controller
public class TemplateController {

    @Resource
    private DiscussService discussService;

    @Resource
    private UserService userService;

    @GetMapping("/discuss/list/")
    public String discuss(Model model) {
        List<DiscussDO> discussDOList = discussService.listDiscuss(0, 10);
        model.addAttribute("discussList", DiscussConvert.convertToDiscussVoList(discussDOList));
        return "discuss";
    }

    @GetMapping("/discuss/item/")
    public String discussItem(
            @RequestParam Integer discussId,
            @RequestParam(required = false,defaultValue = "0")Integer start,
            @RequestParam(required = false,defaultValue = "10")Integer limit,
            Model model
    ){
       DiscussDO discussDO = discussService.findDiscussById(discussId);
       model.addAttribute("discuss",discussDO);
        List<DiscussItemDO> discussItemDOList = discussService.queryDiscussItem(discussId,start,limit);
        List<DiscussItemVo> result = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(discussItemDOList)){
            for (DiscussItemDO discussItemDO : discussItemDOList) {
                UserInfo userInfo = userService.queryUserById(discussItemDO.getUserId());
                DiscussItemVo vo = new DiscussItemVo();
                vo.setDiscussItemDO(discussItemDO);
                vo.setUserInfo(userInfo);
                result.add(vo);
            }
        }
        model.addAttribute("discussItemList",result);
        return "discussItem";
    }


}
