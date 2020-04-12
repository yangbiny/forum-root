package cn.pzhu.forum.biz.discuss.application;

import cn.pzhu.forum.controller.vo.DiscussVo;
import cn.pzhu.forum.entity.DiscussDO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DiscussConvert {
    public static List<DiscussVo> convertToDiscussVoList(List<DiscussDO> discussDOList) {
        return Optional.ofNullable(discussDOList)
                .orElse(Collections.emptyList())
                .stream()
                .map(DiscussConvert::convertToDiscuss)
                .collect(Collectors.toList());
    }

    private static DiscussVo convertToDiscuss(DiscussDO discussDO) {
        if (discussDO == null) {
            return null;
        }
        DiscussVo discussVo = new DiscussVo();
        discussVo.setId(discussDO.getId());
        discussVo.setContent(discussDO.getContent());
        discussVo.setTitle(discussDO.getTitle());
        discussVo.setUserId(discussDO.getUserId());
        discussVo.setIntegral(discussDO.getIntegral());
        discussVo.setStatus(discussDO.getStatus());
        discussVo.setCreateTime(discussDO.getCreateTime());
        return discussVo;
    }
}
