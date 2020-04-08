package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.application.exception.IntegralException;
import cn.pzhu.forum.content.IntegralType;
import cn.pzhu.forum.content.IntegralTypes;
import cn.pzhu.forum.dao.IntegralDao;
import cn.pzhu.forum.entity.IntegralDO;
import cn.pzhu.forum.entity.IntegralItemDO;
import cn.pzhu.forum.service.IntegralService;
import javax.annotation.Resource;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author impassivey
 */
@Service("integralService")
public class IntegralServiceImpl implements IntegralService {

  @Resource
  private IntegralDao integralDao;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean incrByUserId(String userId, Integer number, String integralType) {

    if (number == null || number < 0) {
      throw new IntegralException("非法的积分数 ：%s", number);
    }

    if (!EnumUtils.isValidEnum(IntegralType.class, integralType)) {
      throw new IntegralException("非法的积分类型：%s", integralType);
    }

    // 添加积分的信息
    IntegralItemDO integralItemDO = new IntegralItemDO();
    integralItemDO.setIntegralType(integralType);
    integralItemDO.setNum(number);
    integralItemDO.setType(IntegralTypes.OBTAIN.name());
    integralItemDO.setUserId(userId);
    Integer result = integralDao.addIntegralItem(integralItemDO);
    if (result <= 0) {
      throw new IntegralException("添加积分信息失败");
    }

    // 创建或者修改总积分
    IntegralDO integralDO = integralDao.find(userId);
    Integer res = null;
    if (integralDO == null) {
      integralDO = new IntegralDO();
      integralDO.setUserId(userId);
      integralDO.setNum(number);
      res = integralDao.addIntegral(integralDO);
    } else {
      res = integralDao.updateIntegral(userId, number);
    }

    return res != null && res > 0;
  }

  @Override
  public Integer findIntegralByUserId(String userId) {

    IntegralDO integralDO = integralDao.find(userId);

    return integralDO == null ? 0 : integralDO.getNum();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean reduceByUserId(String userId, Integer integral, String integralType) {

    if (integral == null || integral < 0) {
      throw new IntegralException("非法的积分数 ：%s", integral);
    }

    if (!EnumUtils.isValidEnum(IntegralType.class, integralType)) {
      throw new IntegralException("非法的积分类型：%s", integralType);
    }
    // 添加积分的信息
    IntegralItemDO integralItemDO = new IntegralItemDO();
    integralItemDO.setIntegralType(integralType);
    integralItemDO.setNum(integral);
    integralItemDO.setType(IntegralTypes.CONSUME.name());
    integralItemDO.setUserId(userId);
    Integer result = integralDao.addIntegralItem(integralItemDO);
    if (result <= 0) {
      throw new IntegralException("添加积分信息失败");
    }
    Integer res = integralDao.updateIntegral(userId, -integral);
    return res != null && res > 0;
  }

    @Override
    public List<IntegralDO> findAllUserIntegralByAdmin(int start, int limit) {
        return integralDao.queryUserIntegralByAdmin(start,limit);
    }

    @Override
    public List<IntegralItemDO> queryIntegralItemByUserId(String userId, Integer start, Integer limit) {
      return integralDao.queryIntegralItemByUserId(userId,start,limit);
    }
}
