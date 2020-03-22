package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.content.RedisKeyConstant;
import cn.pzhu.forum.dao.MajorDao;
import cn.pzhu.forum.entity.Major;
import cn.pzhu.forum.service.MajorService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @author impassivey
 */
@Slf4j
@Service("majorService")
public class MajorServiceImpl implements MajorService {

    @Autowired
    private MajorDao majorDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @SuppressWarnings("unchecked")
    public List<Major> list(int id) {

        log.info("根据学院ID查询该学院下的专业信息：{}", id);
        //指定学院下的专业
        String key = RedisKeyConstant.MAJOR_LIST + id;
        //所有专业
        String keyList = RedisKeyConstant.MAJORS;
        // 存放所有的专业
        List<Major> majorList = null;
        ValueOperations<String, List<Major>> operations = redisTemplate.opsForValue();
        redisTemplate.expire(key, 1, TimeUnit.MILLISECONDS);
        redisTemplate.expire(keyList, 1, TimeUnit.MILLISECONDS);
        Boolean hasKey = redisTemplate.hasKey(key);
        // 有指定学院下的专业
        if (hasKey != null && hasKey) {
            log.info("直接从缓存中查询专业信息");
            List<Major> majors = operations.get(key);
            log.info("查询数据长度：{}", majors == null ? 0 : majors.size());
            return majors;
        }
        Boolean hasKey1 = redisTemplate.hasKey(keyList);
        List<Major> list = new ArrayList<>();
        // 有所有专业
        if (hasKey1 != null && hasKey1) {
            log.info("从缓存中查询所有的专业信息");
            majorList = operations.get(keyList);
        } else {
            majorList = majorDao.list();
            if (majorList != null) {
                operations.set(keyList, list);
            }
        }
        if (majorList != null) {
            // 找出所有专业中，schoolId 等于指定ID的专业并添加到list中
            majorList.stream().filter((major) -> major.getSchoolId() == id).forEach(list::add);
            operations.set(key, list);
        }
        return list;
    }

    @Override
    public boolean add(Major major) {

        boolean flag = majorDao.add(major);
        if (flag) {
            List<Major> list = majorDao.list();
            if (list != null) {
                ValueOperations<String, List<Major>> valueOperations = redisTemplate.opsForValue();
                valueOperations.set("major", list);
            }
        }

        return flag;
    }

    @Override
    public boolean delete(int id) {

        boolean flag = majorDao.delete(id);

        if (flag) {
            List<Major> list = majorDao.list();
            if (list != null) {
                ValueOperations<String, List<Major>> valueOperations = redisTemplate.opsForValue();
                valueOperations.set("major", list);
            }
        }
        return flag;
    }

    @Override
    public boolean update(Major major) {

        boolean flag = majorDao.update(major);
        if (flag) {
            List<Major> list = majorDao.list();
            if (list != null) {
                ValueOperations<String, List<Major>> valueOperations = redisTemplate.opsForValue();
                valueOperations.set("major", list);
            }
        }

        return flag;
    }
}
