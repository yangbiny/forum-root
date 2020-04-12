package cn.pzhu.forum.controller;

import cn.pzhu.forum.biz.discuss.application.DiscussCmd;
import cn.pzhu.forum.biz.discuss.service.DiscussService;
import cn.pzhu.forum.controller.form.DiscussForm;
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

    @PostMapping("add/")
    public Resp<Boolean> createDiscuss(
            @RequestBody @Valid DiscussForm form,
            Errors errors){

        if(errors.hasErrors()){
            return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM,errors.getAllErrors().get(0).getDefaultMessage());
        }
        Subject subject = SecurityUtils.getSubject();
        if(subject == null){
            return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM,"未登录");
        }
        Object principal = subject.getPrincipal();
        if(principal == null){
            return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM,"未登录");
        }
        Boolean status = discussService.addDiscuss(toDiscussCmd(form),principal.toString());
        return new Resp<>(status);
    }

    private DiscussCmd toDiscussCmd(DiscussForm form) {
        DiscussCmd discussCmd = new DiscussCmd();
        discussCmd.setTitle(form.getTitle());
        discussCmd.setContent(form.getContent());
        discussCmd.setIntegral(form.getIntegral());
        return discussCmd;
    }

}
