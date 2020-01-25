package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.dao.SchoolDao;
import cn.pzhu.forum.entity.School;
import cn.pzhu.forum.service.SchoolService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @author impassivey
 */
@Service("schoolService")
public class SchoolServiceImpl implements SchoolService {


    @Autowired
    private SchoolDao schoolDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<School> list() {

        String key = "schoolList";
        ValueOperations<String, List<School>> operations = redisTemplate.opsForValue();

        Boolean hasKey = redisTemplate.hasKey(key);

        if (hasKey) {
            List<School> schools = operations.get(key);
            return schools;
        }

        List<School> list = schoolDao.list();

        if (list != null) {
            operations.set(key, list, 1, TimeUnit.MINUTES);
        }

        return list;
    }

    @Override
    public boolean add(String name) {

        boolean flag = schoolDao.add(name);

        if (flag) {
            List<School> list = schoolDao.list();
            if (list != null) {
                String key = "schoolList";
                ValueOperations<String, List<School>> operations = redisTemplate.opsForValue();
                operations.set(key, list, 1, TimeUnit.MINUTES);
            }

        }

        return flag;
    }

    @Override
    public boolean update(School school) {

        boolean flag = schoolDao.update(school);

        if (flag) {
            List<School> list = schoolDao.list();
            if (list != null) {
                String key = "schoolList";
                ValueOperations<String, List<School>> operations = redisTemplate.opsForValue();
                operations.set(key, list, 1, TimeUnit.MINUTES);
            }

        }

        return flag;
    }

    @Override
    public boolean delete(int id) {
        boolean flag = schoolDao.delete(id);

        if (flag) {
            List<School> list = schoolDao.list();
            if (list != null) {
                String key = "schoolList";
                ValueOperations<String, List<School>> operations = redisTemplate.opsForValue();
                operations.set(key, list, 1, TimeUnit.MINUTES);
            }

        }

        return flag;
    }
}
