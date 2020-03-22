package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.content.RedisKeyConstant;
import cn.pzhu.forum.dao.UserInfoDao;
import cn.pzhu.forum.entity.UserInfo;
import cn.pzhu.forum.service.UserInfoService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * 用户模块功能服务
 *
 * @author yangb
 */
@Slf4j
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserInfo get(String id) {

        log.info("cn.pzhu.forum.service.impl.UserInfoServiceImpl.get-获得用户信息-入参-Id = {}", id);

        //从缓存中找
        String key = RedisKeyConstant.USER_USERINFO + id;

        ValueOperations<String, UserInfo> operations = redisTemplate.opsForValue();
        redisTemplate.expire(key, 1, TimeUnit.MINUTES);

        Boolean hasKey = redisTemplate.hasKey(key);

        if (hasKey != null && hasKey) {
            return operations.get(key);
        }

        //从数据库中找
        UserInfo userInfo = userInfoDao.get(id);

        if (userInfo != null) {
            operations.set(key, userInfo, 1, TimeUnit.HOURS);
        }

        return userInfo;
    }

    @Override
    public List<UserInfo> list() {

        log.info("cn.pzhu.forum.service.impl.UserInfoServiceImpl.list-获得所有用户信息-无入参");

        String key = RedisKeyConstant.USERINFO_LIST;

        Boolean hasKey = redisTemplate.hasKey(key);
        SetOperations operations = redisTemplate.opsForSet();
        redisTemplate.expire(key, 1, TimeUnit.HOURS);

        if (hasKey != null && hasKey) {

            log.info("从缓存中查询数据");
            // 获得所有的元素    存入的是list集合，取出来也是list集合
            Set<UserInfo> members = operations.members(key);
            Iterator<UserInfo> iterator = members.iterator();

            List<UserInfo> list = new ArrayList<>();

            while (iterator.hasNext()) {

                list.add(iterator.next());
            }

            return list;
        }

        List<UserInfo> list = userInfoDao.list();

        list.forEach((userInfo -> operations.add(key, userInfo)));

        return list;
    }

    @Override
    public int userCount() {
        return userInfoDao.userCount();
    }

    @Override
    public List<UserInfo> selectUserBySearch(String text, Integer start, int limit) {
        text = "%"+text+"%";
        return userInfoDao.selectBySearch(text,start,limit);
    }
}
