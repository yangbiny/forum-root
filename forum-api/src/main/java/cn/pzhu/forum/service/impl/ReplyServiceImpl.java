package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.content.RedisKeyConstant;
import cn.pzhu.forum.dao.ReplyDao;
import cn.pzhu.forum.entity.Reply;
import cn.pzhu.forum.service.ReplyService;
import cn.pzhu.forum.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: forum-root
 * @description: 博客回复信息具体实现
 * @author: Impassive
 * @create: 2019-05-21 09:10
 **/
@Service
@Slf4j
@SuppressWarnings("unchecked")
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyDao replyDao;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询对应文章下的评论及回复
     *
     * @param id 文章ID
     * @return 对应文章下的所有评论信息
     */
    @Override
    public List<Reply> list(Integer id) {

        log.info("cn.pzhu.forum.service.impl.ReplyServiceImpl.list-查询对应文章下的评论及回复-入参：" +
                "id = {}", id);

        String key = RedisKeyConstant.ARTICLE_REPLY_LIST + id;

        Boolean hasKey = redisTemplate.hasKey(key);
        ValueOperations operations = redisTemplate.opsForValue();

        if (hasKey != null && hasKey) {
            List<Reply> o = (List<Reply>) operations.get(key);

            log.info("从缓存中查询数据-数据长度 = {}", o.size());

            return o;
        }


        List<Reply> list = replyDao.list(id);
        log.info("从数据库中查询数据-数据长度- {}", list == null ? 0 : list.size());
        if (list != null) {
            operations.set(key, list, 1, TimeUnit.HOURS);
        }

        return list;
    }

    /**
     * 添加新的留言信息，如果留言信息添加成功，则更新缓存，否则直接返回false。
     *
     * @param reply 留言内容
     * @return 操作结果
     */
    @Override
    public boolean add(Reply reply) {
        log.info("cn.pzhu.forum.service.impl.ReplyServiceImpl.add-添加新的回复信息-入参：{}", reply.toString());

        reply.setTime(Utils.getDate(false));

        Integer add = replyDao.add(reply);

        if (add > 0) {

            String key = RedisKeyConstant.ARTICLE_REPLY_LIST + reply.getArticleId();

            Boolean hasKey = redisTemplate.hasKey(key);
            ValueOperations operations = redisTemplate.opsForValue();

            if (hasKey != null && hasKey) {
                List<Reply> o = (List<Reply>) operations.get(key);
                o.add(reply);
                operations.set(key, o, 1, TimeUnit.HOURS);
            } else {

                List<Reply> list = new ArrayList<>();
                list.add(reply);
                operations.set(key, list, 1, TimeUnit.HOURS);
            }
            log.info("cn.pzhu.forum.service.impl.ReplyServiceImpl.add-添加新的回复信息-成功：{}", reply.toString());
            return true;

        }
        log.info("cn.pzhu.forum.service.impl.ReplyServiceImpl.add-添加新的回复信息-失败");
        return false;
    }

    @Override
    public int replyCount() {

        return replyDao.replyCount();
    }
}
