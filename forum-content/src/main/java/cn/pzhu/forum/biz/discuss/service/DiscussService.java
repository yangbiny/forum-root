package cn.pzhu.forum.biz.discuss.service;

import cn.pzhu.forum.biz.discuss.application.DiscussCmd;
import cn.pzhu.forum.biz.discuss.application.DiscussReplyCmd;
import cn.pzhu.forum.content.DiscussStatus;
import cn.pzhu.forum.content.IntegralType;
import cn.pzhu.forum.dao.DiscussDao;
import cn.pzhu.forum.entity.DiscussDO;
import cn.pzhu.forum.entity.DiscussItemDO;
import cn.pzhu.forum.entity.UserInfo;
import cn.pzhu.forum.service.IntegralService;
import cn.pzhu.forum.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author impassivey
 */
@Service
public class DiscussService {

    @Resource
    private DiscussDao discussDao;

    @Resource
    private UserService userService;

    @Resource
    private IntegralService integralService;

    public List<DiscussDO> listDiscuss(Integer start, Integer limit) {
        if (start == null || limit == null) {
            start = 0;
            limit = 10;
        }
        return discussDao.listDiscuss(start, limit);
    }

    public Boolean addDiscuss(DiscussCmd cmd, String principal) {
        DiscussDO discussDO = toDiscussDo(cmd);
        discussDO.setUserId(principal);
        discussDO.setCreateTime(new Date());
        discussDO.setStatus(DiscussStatus.INIT.getCode());
        return discussDao.addDiscuss(discussDO);
    }

    private DiscussDO toDiscussDo(DiscussCmd cmd) {
        DiscussDO discussDO = new DiscussDO();
        discussDO.setTitle(cmd.getTitle());
        discussDO.setContent(cmd.getContent());
        discussDO.setIntegral(cmd.getIntegral());
        return discussDO;
    }

    public List<DiscussItemDO> queryDiscussItem(Integer discussId, Integer start, Integer limit) {
        return discussDao.listDiscussItem(discussId, start, limit);
    }

    public DiscussDO findDiscussById(Integer discussId) {
        return discussDao.findDiscussById(discussId);
    }

    public Boolean addDiscussReply(DiscussReplyCmd cmd) {
        return discussDao.addDiscussReply(toDiscussReply(cmd));
    }

    private DiscussItemDO toDiscussReply(DiscussReplyCmd cmd) {
        DiscussItemDO discussItemDO = new DiscussItemDO();
        discussItemDO.setDiscussId(cmd.getDiscussId());
        discussItemDO.setContent(cmd.getContent());
        discussItemDO.setUserId(cmd.getUserId());
        discussItemDO.setStatus(DiscussStatus.INIT.getCode());
        discussItemDO.setCreateTime(new Date());
        return discussItemDO;
    }

    public DiscussItemDO findDiscussItemById(Integer discussItemId) {
        return discussDao.findDiscussItemById(discussItemId);
    }
}
