package cn.pzhu.forum.controller;

import cn.pzhu.forum.controller.vo.IntegralVo;
import cn.pzhu.forum.entity.IntegralDO;
import cn.pzhu.forum.service.IntegralService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("integral")
    public String integral(Model model) {
        int start = 0;
        int limit = 24;
        List<IntegralDO> integralDOS = integralService.findAllUserIntegralByAdmin(start, limit);
        if (CollectionUtils.isEmpty(integralDOS)) {
            integralDOS = Collections.emptyList();
        }
        List<IntegralVo> integralVos = convertToIntegralVoList(integralDOS);
        if(CollectionUtils.size(integralVos) >= limit){
            model.addAttribute("hasMore",true);
            model.addAttribute("nextStart",limit+start);
        }
        model.addAttribute("integralList",integralVos);
        return "integral";
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
        integralVo.setNumber(integralDO.getNum());
        integralVo.setUserId(integralDO.getUserId());
        return integralVo;
    }


}
