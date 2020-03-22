package cn.pzhu.forum.service;

import cn.pzhu.forum.entity.UserInfo;

import java.util.List;

public interface UserInfoService {

    /**
     * 查询用户信息
     *
     * @param id 用户ID
     * @return 查询出来的用户信息
     */
    UserInfo get(String id);

    /**
     * 获得所有的用户信息
     *
     * @return 用户信息集合
     */
    List<UserInfo> list();

    /**
     * 统计用户数量
     *
     * @return 用户数量
     */
    int userCount();

    List<UserInfo> selectUserBySearch(String text, Integer start, int limit);
}
