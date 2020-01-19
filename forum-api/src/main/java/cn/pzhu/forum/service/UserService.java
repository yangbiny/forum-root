package cn.pzhu.forum.service;

import cn.pzhu.forum.entity.User;
import cn.pzhu.forum.entity.UserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    /**
     * 用户注册,包括完善用户名信息
     *
     * @param user 用户注册信息
     * @return 注册结果
     */
    boolean addUser(User user, String username);

    /**
     * 密码重置
     *
     * @param user 需要重置密码的用户
     * @return 重置结果
     */
    boolean resetPassword(User user);

    /**
     * 更新用户头像信息
     *
     * @param id            用户ID
     * @param multipartFile 头像文件
     * @param path          存储位置
     * @return 操作结果
     */
    String updateUserAvatar(String id, MultipartFile multipartFile, String path);

    /**
     * 更新用户信息
     *
     * @param userInfo 需要更新的用户的信息
     * @return 更新结果
     */
    boolean updateUserInfo(UserInfo userInfo);

    /**
     * 删除用户，逻辑删除，将用户状态修改为-1死亡状态
     *
     * @param id 用户ID
     * @return 操作结果
     */
    boolean delete(String id);

    /**
     * 根据用户的QQ 的openID查询用户信息
     *
     * @param openId 用户QQ的OpenID
     * @return 用户信息
     */
    User get(String openId);

    /**
     * 检查用户名是否被使用
     *
     * @param userName 用户名
     * @return false:没有被使用，true:已经被使用
     */
    boolean checkUserName(String userName);

}
