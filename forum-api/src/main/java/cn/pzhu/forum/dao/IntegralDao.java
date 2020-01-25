package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.IntegralDO;
import cn.pzhu.forum.entity.IntegralItemDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

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
   * @param num 需要添加的积分的数量
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


}
