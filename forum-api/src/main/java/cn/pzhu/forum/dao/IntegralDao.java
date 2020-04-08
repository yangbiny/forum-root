package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.IntegralDO;
import cn.pzhu.forum.entity.IntegralItemDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author impassivey
 */
@Mapper
@Repository
public interface IntegralDao {

    /**
     * 添加积分信息
     *
     * @param integralItemDO 需要添加的积分的信息
     * @return 大于0，添加成功
     */
    @Insert("insert into integral_item set userId = #{userId},integral_type = #{integralType},"
            + "num = #{num}, time = now(), type = #{type}")
    Integer addIntegralItem(IntegralItemDO integralItemDO);


    /**
     * 添加总的积分信息
     *
     * @param integralDO 需要添加的积分的信息
     * @return 大于0时代表添加成功
     */
    @Insert("insert into integral set userId = #{userId}, num = #{num}")
    Integer addIntegral(IntegralDO integralDO);


    /**
     * 更新用户的积分信息
     *
     * @param userId 用户ID
     * @param num    需要添加的积分的数量
     * @return 大于0是代表更新成功
     */
    @Update("update integral set num = num + #{num} where userId = #{userId}")
    Integer updateIntegral(@Param("userId") String userId, @Param("num") Integer num);


    /**
     * 查找用户的积分信息
     *
     * @param userId 用户ID
     * @return 用户的积分信息
     */
    @Select("select id,userId,num from integral where userId = #{userId}")
    @Results(id = "integral", value = {
            @Result(column = "userId", property = "userId"),
            @Result(column = "num", property = "num"),
            @Result(column = "id", property = "id")
    })
    IntegralDO find(@Param("userId") String userId);

    @Select("select id,userId,num from integral limit #{start},#{limit}")
    @ResultMap(value = {"integral"})
    List<IntegralDO> queryUserIntegralByAdmin(@Param("start") int start, @Param("limit") int limit);

    @Select("select id,userId,num,integral_type,type,time from integral_item where userId = #{userId}" +
            "limit #{start},#{limit}")
    @Results(id = "integralItem", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "userId", property = "userId"),
            @Result(column = "integral_type", property = "integralType"),
            @Result(column = "type", property = "type"),
            @Result(column = "num", property = "num"),
            @Result(column = "time", property = "time")
    })
    List<IntegralItemDO> queryIntegralItemByUserId(@Param("userId") String userId, @Param("start") Integer start, @Param("limit") Integer limit);
}
