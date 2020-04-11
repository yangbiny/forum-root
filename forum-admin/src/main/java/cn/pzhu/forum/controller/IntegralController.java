package cn.pzhu.forum.controller;

import cn.pzhu.forum.controller.vo.IntegralItemVo;
import cn.pzhu.forum.controller.vo.IntegralVo;
import cn.pzhu.forum.entity.IntegralDO;
import cn.pzhu.forum.entity.IntegralItemDO;
import cn.pzhu.forum.service.IntegralService;
import cn.pzhu.forum.service.UserService;
import cn.pzhu.forum.util.Resp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Controller
@RequestMapping("admin/")
public class IntegralController {

    @Resource
    private IntegralService integralService;

    @Resource
    private UserService userService;

    @GetMapping("integral")
    public String integral(Model model) {
        int start = 0;
        int limit = 24;
        List<IntegralDO> integralDOS = integralService.findAllUserIntegralByAdmin(start, limit);
        if (CollectionUtils.isEmpty(integralDOS)) {
            integralDOS = Collections.emptyList();
        }
        List<IntegralVo> integralVos = convertToIntegralVoList(integralDOS);
        if (CollectionUtils.size(integralVos) >= limit) {
            model.addAttribute("hasMore", true);
            model.addAttribute("nextStart", limit + start);
        }
        model.addAttribute("integralList", integralVos);
        return "integral";
    }

    @GetMapping("integral/list/")
    @ResponseBody
    public Resp<List<IntegralVo>> queryUserIntegralListForAdmin(
            @RequestParam(required = false, defaultValue = "0") Integer start,
            @RequestParam(required = false, defaultValue = "24") Integer limit
    ) {
        List<IntegralDO> integral = integralService.findAllUserIntegralByAdmin(start, limit);
        List<IntegralVo> integralVos = convertToIntegralVoList(integral);
        Resp<List<IntegralVo>> listResp = new Resp<>(integralVos);
        if(integralVos.size() == limit){
            listResp.setHasMore(true);
            listResp.setNextStart(start+limit);
        }
        return listResp;
    }

    @GetMapping("integral/user/list/")
    @ResponseBody
    public Resp<List<IntegralItemVo>> queryUserIntegralItemByUserId(
            @RequestParam String integralId,
            @RequestParam(required = false, defaultValue = "0") Integer start,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ){
        if(StringUtils.isEmpty(integralId)){
            return new Resp<>(Resp.RespStatus.INTERNAL_ERROR);
        }
        List<IntegralItemDO> integralItemDOList = integralService.queryIntegralItemByIntegralId(integralId,start,limit);
        List<IntegralItemVo> integralItemVos = convertToIntegralItemVoList(integralItemDOList);
        Resp<List<IntegralItemVo>> listResp = new Resp<>(integralItemVos);
        if(integralItemVos.size() == limit){
            listResp.setHasMore(true);
            listResp.setNextStart(start+limit);
        }
        return listResp;
    }

    @ResponseBody
    @GetMapping("integral/by_search/")
    public Resp<List<IntegralVo>> queryByUserId(
            @RequestParam String userId
    ){
        List<IntegralDO> integralDOS = integralService.queryIntegralByUserId(userId);
        List<IntegralVo> integralVos = convertToIntegralVoList(integralDOS);
        return new Resp<>(integralVos);
    }

    private List<IntegralItemVo> convertToIntegralItemVoList(List<IntegralItemDO> integralItemDOList) {
        return Optional.ofNullable(integralItemDOList)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::convertToIntegralItemVo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private IntegralItemVo convertToIntegralItemVo(IntegralItemDO integralItemDO) {
        if(integralItemDO == null){
            return null;
        }
        IntegralItemVo vo = new IntegralItemVo();
        vo.setId(integralItemDO.getId());
        vo.setIntegralType(integralItemDO.getIntegralType());
        vo.setNum(integralItemDO.getNum());
        vo.setTime(integralItemDO.getTime());
        vo.setUserId(integralItemDO.getUserId());
        vo.setType(integralItemDO.getType());
        return vo;
    }

    private List<IntegralVo> convertToIntegralVoList(List<IntegralDO> integralDOS) {
        return Optional.ofNullable(integralDOS)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::convertToIntegralVo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private IntegralVo convertToIntegralVo(IntegralDO integralDO) {
        if (integralDO == null) {
            return null;
        }
        IntegralVo integralVo = new IntegralVo();
        integralVo.setId(integralDO.getId());
        integralVo.setNumber(integralDO.getNum());
        integralVo.setUserId(integralDO.getUserId());
        return integralVo;
    }


}
