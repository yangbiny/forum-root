package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.User;
import cn.pzhu.forum.entity.UserInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {

    /**
     * Add a new user
     *
     * @param user User Infomation
     * @return The Result of the action
     */
    @Insert("insert into user (id, password,qq) values (#{id}, #{password},#{qq})")
    boolean add(User user);

    /**
     * Query the user with the specified ID
     *
     * @param id User Id
     * @return User Info
     */
    @Select("select id,qq,password,status,identify from user where id = #{id} or qq = #{id}")
    User get(@Param("id") String id);

    /**
     * Reset User password
     *
     * @param user User
     * @return Status
     */
    @Update("update user set password = #{password} where id = #{id}")
    boolean resetPassword(User user);

    /**
     * 更新用户头像
     *
     * @param id   用户ID
     * @param path 头像存放路径
     * @return true表示更新成功，false表示更新失败
     */
    @Update("update userInfo set avatar = #{path} where id = #{id} ")
    boolean updateUserAvatar(@Param("id") String id, @Param("path") String path);

    /**
     * 更新用户信息
     *
     * @param userInfo 需要更新的用户信息
     * @return true表示更新成功，false表示更新失败
     */
    @Update("update userInfo set major = #{major},school = #{school}," +
            "nickName = #{nickName},phone = #{phone},description = #{description} " +
            "where id = #{id}")
    boolean updateUserInfo(UserInfo userInfo);

    /**
     * 更新用户状态，-1为不可用状态，实现逻辑删除用户
     *
     * @param id 用户ID
     * @return 操作结果
     */
    @Update("update user set status = -1 where id = #{id}")
    boolean delete(@Param("id") String id);

    /**
     * 用户使用QQ登录时查询用户信息
     *
     * @param openid QQ的OpenID
     * @return 查询结果
     */
    @Select("select id,qq,password,status,identify from user where qq = #{qq}")
    User getByOpenId(@Param("qq") String openid);

    /**
     * 用户绑定QQ
     *
     * @param user 用户信息
     * @return 操作结果
     */
    @Update("update user set qq = #{qq} where id = #{id}")
    Boolean updateQQ(User user);
}
