package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.content.DiscussStatus;
import cn.pzhu.forum.content.IntegralType;
import cn.pzhu.forum.dao.DiscussDao;
import cn.pzhu.forum.entity.DiscussDO;
import cn.pzhu.forum.entity.DiscussItemDO;
import cn.pzhu.forum.entity.UserInfo;
import cn.pzhu.forum.service.DiscussItemService;
import cn.pzhu.forum.service.IntegralService;
import cn.pzhu.forum.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("discussItemService")
public class DiscussItemServiceImpl implements DiscussItemService {

    @Resource
    private UserService userService;

    @Resource
    private DiscussDao discussDao;

    @Resource
    private IntegralService integralService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean adoption(DiscussItemDO discussItemDO) {
        String userId = discussItemDO.getUserId();
        UserInfo userInfo = userService.queryUserById(userId);
        if (userInfo == null) {
            return false;
        }
        DiscussDO discussById = discussDao.findDiscussById(discussItemDO.getDiscussId());
        if (discussById == null) {
            return false;
        }
        Boolean result = discussDao.updateDiscussStatus(discussById.getId(), DiscussStatus.ADOPTION.getCode());
        result = discussDao.updateDiscussItemStatus(discussItemDO.getId(), DiscussStatus.ADOPTION.getCode());
        if (result) {
            result = integralService.incrByUserId(userId, discussById.getIntegral(), IntegralType.ADOPTION.name());
            if (result) {
                result = integralService.reduceByUserId(userId, discussById.getIntegral(), IntegralType.HELP.name());
            }
        }
        return result;
    }

}
