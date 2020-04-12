package cn.pzhu.forum.controller;

import cn.pzhu.forum.biz.discuss.application.DiscussCmd;
import cn.pzhu.forum.biz.discuss.application.DiscussReplyCmd;
import cn.pzhu.forum.biz.discuss.service.DiscussService;
import cn.pzhu.forum.controller.form.DiscussAdoptionForm;
import cn.pzhu.forum.controller.form.DiscussForm;
import cn.pzhu.forum.controller.form.DiscussReplyForm;
import cn.pzhu.forum.entity.DiscussItemDO;
import cn.pzhu.forum.service.DiscussItemService;
import cn.pzhu.forum.util.Resp;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author impassivey
 */
@RestController
@RequestMapping("/user/discuss/")
public class DiscussController {

    @Resource
    private DiscussService discussService;

    @Resource
    private DiscussItemService discussItemService;

    @PostMapping("add/")
    public Resp<Boolean> createDiscuss(
            @RequestBody @Valid DiscussForm form,
            Errors errors) {

        if (errors.hasErrors()) {
            return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM, errors.getAllErrors().get(0).getDefaultMessage());
        }
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) {
            return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM, "未登录");
        }
        Object principal = subject.getPrincipal();
        if (principal == null) {
            return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM, "未登录");
        }
        Boolean status = discussService.addDiscuss(toDiscussCmd(form), principal.toString());
        return new Resp<>(status);
    }

    @PostMapping("reply/")
    public Resp<Boolean> discussReply(
            @RequestBody @Valid DiscussReplyForm form,
            Errors errors) {
        if (errors.hasErrors()) {
            return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM, errors.getAllErrors().get(0).getDefaultMessage());
        }
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) {
            return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM, "未登录");
        }
        Object principal = subject.getPrincipal();
        if (principal == null) {
            return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM, "未登录");
        }
        Boolean status = discussService.addDiscussReply(toDiscussReplyCmd(form, principal.toString()));
        return new Resp<>(status);
    }

    @PostMapping("adoption/")
    public Resp<Boolean> adoption(@RequestBody DiscussAdoptionForm form) {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) {
            return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM, "未登录");
        }
        Object principal = subject.getPrincipal();
        if (principal == null) {
            return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM, "未登录");
        }
       DiscussItemDO discussItemDO = discussService.findDiscussItemById(form.getDiscussItemId());
       if(discussItemDO == null){
           return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM,"找不到对应的信息");
       }
       Boolean status = discussItemService.adoption(discussItemDO);
        return new Resp<>(status);
    }

    private DiscussReplyCmd toDiscussReplyCmd(DiscussReplyForm form, String userId) {
        DiscussReplyCmd discussReplyCmd = new DiscussReplyCmd();
        discussReplyCmd.setDiscussId(form.getDiscussId());
        discussReplyCmd.setContent(form.getContent());
        discussReplyCmd.setUserId(userId);
        return discussReplyCmd;
    }

    private DiscussCmd toDiscussCmd(DiscussForm form) {
        DiscussCmd discussCmd = new DiscussCmd();
        discussCmd.setTitle(form.getTitle());
        discussCmd.setContent(form.getContent());
        discussCmd.setIntegral(form.getIntegral());
        return discussCmd;
    }

}
