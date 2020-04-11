package cn.pzhu.forum.controller;

import cn.pzhu.forum.controller.vo.IntegralItemVo;
import cn.pzhu.forum.entity.IntegralDO;
import cn.pzhu.forum.entity.IntegralItemDO;
import cn.pzhu.forum.service.IntegralService;
import cn.pzhu.forum.util.Resp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author impassivey
 */
@RestController
@RequestMapping("/integral/user/")
public class IntegralController {

    @Resource
    private IntegralService integralService;

    @GetMapping("list/by_userId/")
    public cn.pzhu.forum.util.Resp<List<IntegralItemVo>> queryIntegralByUserId(
            @RequestParam String userId,
            @RequestParam(required = false,defaultValue = "0") Integer start,
            @RequestParam(required = false,defaultValue = "10") Integer limit
    ) {
        if(StringUtils.isEmpty(userId)){
            return new Resp<>(Resp.RespStatus.ILLEGAL_PARAM);
        }
        List<IntegralItemDO> integralItemDOList = integralService.queryIntegralItemByUserId(userId,start,limit);
        List<IntegralItemVo> integralItemVos = convertToIntegralItemVoList(integralItemDOList);
        return new Resp<>(integralItemVos.size() == limit,start + limit,integralItemVos);
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

}
