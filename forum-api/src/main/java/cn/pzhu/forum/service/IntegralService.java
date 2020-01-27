package cn.pzhu.forum.service;

/**
 * @author impassivey
 */
public interface IntegralService {

  /**
   * 增加用户的积分
   *
   * @param userId 用户ID
   * @param number 增加的积分数量
   * @param integralType 积分的类型
   * @return 增加结果
   */
  boolean incrByUserId(String userId, Integer number, String integralType);

  /**
   * 查询用户的总积分信息
   *
   * @param userId 用户ID
   * @return 积分信息
   */
  Integer findIntegralByUserId(String userId);

  /**
   * 减少用户积分
   *
   * @param userId 用户ID
   * @param integral 积分数
   * @param integralType 积分类型
   * @return 操作结果
   */
  boolean reduceByUserId(String userId, Integer integral, String integralType);
}
