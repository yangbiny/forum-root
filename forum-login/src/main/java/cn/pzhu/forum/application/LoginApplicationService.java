package cn.pzhu.forum.application;

import cn.pzhu.forum.content.IntegralType;
import cn.pzhu.forum.service.IntegralService;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @author impassivey
 */
@Service
public class LoginApplicationService {

  private final static String LOGIN_PREFIX = "first_login_";

  @Resource
  private IntegralService integralService;

  @Resource
  private RedisTemplate<String, String> redisTemplate;

  public void whenUserLoginSuccess(String userId) {

    if (StringUtils.isEmpty(userId)) {
      return;
    }

    if (redisTemplate.hasKey(LOGIN_PREFIX + userId)) {
      return;
    }

    boolean result = integralService.incrByUserId(userId, 1, IntegralType.LOGIN.name());

    if (result) {
      ValueOperations<String, String> operations = redisTemplate.opsForValue();
      operations
          .set(LOGIN_PREFIX + userId, Boolean.TRUE.toString(), calculateTime(), TimeUnit.SECONDS);
    }
  }

  private long calculateTime() {

    Calendar calendar = Calendar.getInstance();
    Date now = calendar.getTime();
    long timeOfNow = now.getTime();

    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
    long timeOfFeature = calendar.getTime().getTime();

    long div = timeOfFeature - timeOfNow;

    return div / 1000;
  }


}
