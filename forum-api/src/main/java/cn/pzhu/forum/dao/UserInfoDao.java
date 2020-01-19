package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.UserInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserInfoDao {

    /**
     * 查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Select("select id,nickName,school,major,avatar,phone,description from userInfo where id = #{id}")
    UserInfo get(@Param("id") String id);

    /**
     * 查询所有的用户信息
     *
     * @return 用户信息列表
     */
    @Select("select userInfo.id,nickName,school,major,avatar,phone,description from userInfo,user where " +
            "user.id = userInfo.id and user.status != -1")
    List<UserInfo> list();

    /**
     * 查询用户信息总数
     *
     * @return 用户信息总数
     */
    @Select("select count(*) from userInfo")
    int userCount();

    /**
     * 查询学院用户注册信息
     * （借助学院ID保存注册人数，用户昵称保存学院名字）
     *
     * @return 用户注册信息
     */
    @Select("select school.name schoolName,count(userinfo.school) number from school LEFT JOIN userinfo " +
            "on userinfo.school = school.id GROUP BY schoolName")
    @Results(id = "userinfo", value = {
            @Result(property = "school", column = "number"),
            @Result(property = "nickName", column = "schoolName"),
    })
    List<UserInfo> registerCount();

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户的信息
     */
    @Select("select id,nickName,school,major,avatar,phone,description from userInfo where nickName = #{username}")
    UserInfo getByName(@Param("username") String username);

    /**
     * 添加用户信息
     *
     * @param username 用户名信息
     * @param id       用户ID
     * @return 添加结果
     */
    @Insert("insert into userinfo set id = #{id},nickName = #{username}")
    boolean add(@Param("username") String username, @Param("id") String id);

}
