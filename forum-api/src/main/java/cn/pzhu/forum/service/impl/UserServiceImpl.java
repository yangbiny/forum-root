package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.content.Identify;
import cn.pzhu.forum.content.RedisKeyConstant;
import cn.pzhu.forum.dao.UserDao;
import cn.pzhu.forum.dao.UserInfoDao;
import cn.pzhu.forum.entity.User;
import cn.pzhu.forum.entity.UserInfo;
import cn.pzhu.forum.service.UserService;
import cn.pzhu.forum.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author impassivey
 */
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addUser(User user, String username) {

        log.info("cn.pzhu.forum.service.impl.UserServiceImpl.addUser-用户注册-入参");

        if (user == null) {
            return false;
        }

        boolean flag;

        User oldUser = userDao.get(user.getUserId());

        // 该邮箱没有注册，就可以直接使用注册
        if (oldUser == null) {
            String string = new Md5Hash(user.getPassword(), user.getUserId(), 1024).toString();
            user.setPassword(string);
            flag = userDao.add(user);
        } else {  // 通过QQ登录后注册或者绑定
            // 被管理员删除的账号不能再次使用
            if (oldUser.getStatus().equals(Identify.DEAD.getIdentify())) {
                return false;
            }
            flag = userDao.updateQQ(user);
        }
        if (flag) {
            flag = userInfoDao.add(username, user.getId());
        }
        log.info("cn.pzhu.forum.service.impl.UserServiceImpl.addUser-注册结果-{}", flag);
        return flag;
    }

    @Override
    public boolean resetPassword(User user) {
        log.info("cn.pzhu.forum.service.impl.UserServiceImpl.resetPassword-用户重置密码");

        String string = new Md5Hash(user.getPassword(), user.getId(), 1024).toString();
        user.setPassword(string);
        boolean b = userDao.resetPassword(user);

        log.info("cn.pzhu.forum.service.impl.UserServiceImpl.resetPassword-操作结果-{}", b);
        return b;
    }

    @Override
    public String updateUserAvatar(String id, MultipartFile multipartFile, String path) {

        ByteArrayInputStream inputStream = null;
        try {
            inputStream = (ByteArrayInputStream) multipartFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        path = UUID.randomUUID().toString().substring(0,5)+path;
        String paths = Utils.uploadImg(inputStream, path);
        boolean b1;
        if (!"".equals(paths)) {

            b1 = userDao.updateUserAvatar(id, paths);
            if (b1) {
                String key = RedisKeyConstant.USER_USERINFO + id;
                redisTemplate.expire(key, 1, TimeUnit.SECONDS);
                ValueOperations operations = redisTemplate.opsForValue();
                // 判断缓存中是否已经有了该用户的信息
                Boolean hasKey = redisTemplate.hasKey(key);

                // 如果有该用户的信息，就取出来并更新，没有则放弃
                if (hasKey != null && hasKey) {
                    UserInfo userInfo = (UserInfo) operations.get(key);
                    userInfo.setAvatar(paths);
                    operations.set(key, userInfo);
                }
            }
        }
        return paths;
    }

    @Override
    public boolean updateUserInfo(UserInfo userInfo) {

        log.info("cn.pzhu.forum.service.impl.UserServiceImpl.updateUserInfo-更新用户信息-入参-" +
                "userInfo = {}", userInfo.toString());

        boolean b = userDao.updateUserInfo(userInfo);

        log.info("更新用户信息结果：" + b);
        if (b) {

            String key = RedisKeyConstant.USERINFO_LIST;
            Boolean hasKey = redisTemplate.hasKey(key);
            redisTemplate.expire(key, 1, TimeUnit.HOURS);

            // 如果缓存中已经存在有用户信息列表，则添加到用户信息列表中
            if (hasKey != null && hasKey) {

                log.info("更新用户信息后更新缓存数据");

                SetOperations operations = redisTemplate.opsForSet();
                operations.add(key, userInfo);

                log.info("更新缓存成功!");
            }
        }

        return b;
    }

    @Override
    public boolean delete(String id) {

        return userDao.delete(id);
    }

    @Override
    public User get(String openId) {
        log.info("cn.pzhu.forum.service.impl.UserServiceImpl.get-用户使用QQ登录");

        User user = userDao.getByOpenId(openId);

        log.info("cn.pzhu.forum.service.impl.UserServiceImpl.get-用户使用QQ登录查询结果-是否为空：{}", user == null);
        return user;
    }

    @Override
    public boolean checkUserName(String userName) {

        UserInfo byName = userInfoDao.getByName(userName);

        return byName == null;
    }

    @Override
    public UserInfo queryUserById(String userId) {
        UserInfo userInfo = userInfoDao.get(userId);
        return userInfo;
    }
}
