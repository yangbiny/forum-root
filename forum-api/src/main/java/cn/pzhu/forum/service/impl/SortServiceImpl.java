package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.content.ArticleType;
import cn.pzhu.forum.content.RedisKeyConstant;
import cn.pzhu.forum.dao.SortDao;
import cn.pzhu.forum.entity.Sort;
import cn.pzhu.forum.service.SortService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @author impassivey
 */
@Service
@Slf4j
public class SortServiceImpl implements SortService {

    @Resource
    private SortDao sortDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public List<Sort> list() {

        log.info("cn.pzhu.forum.service.impl.SortServiceImpl.list-查询所有分类信息-无入参");

        String key = RedisKeyConstant.SORT_LIST;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        redisTemplate.expire(key, 1, TimeUnit.HOURS);
        Boolean hasKey = redisTemplate.hasKey(key);

        if (hasKey != null && hasKey) {
            List<Sort> list = (List<Sort>) operations.get(key);
            log.info("从缓存查询所有的分类信息长度-list size = {}", list.size());
            return list;
        }

        List<Sort> list = sortDao.list();

        if (list != null) {
            operations.set(key, list);
        }

        return list;
    }

    @Override
    public Sort get(int id) {
        return null;
    }

    @Override
    public boolean update(Sort sort) {
        log.info("cn.pzhu.forum.service.impl.SortServiceImpl.update-更新分类信息-入参-sort = {}", sort.toString());
        boolean update = sortDao.update(sort);

        if (update) {
            String key = RedisKeyConstant.SORT_LIST;
            ValueOperations operations = redisTemplate.opsForValue();
            redisTemplate.expire(key, 1, TimeUnit.HOURS);
            List<Sort> list = sortDao.list();
            operations.set(key, list);
        }

        return update;
    }

    @Override
    public boolean add(Sort sort) {

        log.info("cn.pzhu.forum.service.impl.SortServiceImpl.add-添加新的分类-入参-sort = {}", sort.toString());

        boolean add = sortDao.add(sort);

        if (add) {
            String key = RedisKeyConstant.SORT_LIST;
            redisTemplate.expire(key, 1, TimeUnit.HOURS);
            ValueOperations operations = redisTemplate.opsForValue();

            List<Sort> list = sortDao.list();
            operations.set(key, list, 1, TimeUnit.HOURS);
        }

        return add;
    }

    @Override
    public boolean delete(int id) {

        log.info("cn.pzhu.forum.service.impl.SortServiceImpl.delete-删除分类信息-入参-id = {}", id);

        String key = RedisKeyConstant.SORT_LIST;
        ValueOperations operations = redisTemplate.opsForValue();
        redisTemplate.expire(key, 1, TimeUnit.HOURS);
        Boolean hasKey = redisTemplate.hasKey(key);
        List<Sort> list;

        if (hasKey != null && hasKey) {
            list = (List<Sort>) operations.get(key);
        } else {

            list = sortDao.list();
        }

        for (Sort sort : list) {
            // 如果当前分类有其他的子类，不允许删除
            if (sort.getRely() != null && sort.getRely() == id) {
                return false;
            }
        }

        boolean delete = sortDao.delete(id);

        // 删除成功后将该分类从列表中删除
        if (delete) {
            for (Sort sort : list) {

                if (sort.getId() == id) {
                    list.remove(sort);
                    break;
                }
            }
            operations.set(key, list, 1, TimeUnit.HOURS);
        }

        return delete;
    }

    @Override
    public List<Sort> list(ArticleType articleType) {

        String key = RedisKeyConstant.SORT_ARTICLETYPE_LIST + articleType.getType();
        redisTemplate.expire(key, 1, TimeUnit.SECONDS);

        ValueOperations operations = redisTemplate.opsForValue();
        Boolean hasKey = redisTemplate.hasKey(key);

        // 先判断是否有该类型的缓存
        if (hasKey != null && hasKey) {
            return (List<Sort>) operations.get(key);
        }
        List<Sort> list;
        String sortKey = RedisKeyConstant.SORT_LIST;
        redisTemplate.expire(sortKey, 1, TimeUnit.SECONDS);
        hasKey = redisTemplate.hasKey(sortKey);
        // 判断是否有所有的分类信息的集合
        if (hasKey != null && hasKey) {
            list = (List<Sort>) operations.get(sortKey);
        } else {
            list = sortDao.list();
            if (list != null) {
                operations.set(sortKey, list, 1, TimeUnit.SECONDS);
            }
        }
        if (list != null) {
            Map<Integer, Sort> map = new HashMap<>();
            // 表示是否找到一级标题
            boolean flag = true;
            // 用于判断循环次数，避免出现分类信息丢失的情况
            int count = 3;
            do {
                count--;
                for (int i = 0; i < list.size(); i++) {

                    Sort sort = list.get(i);
                    // 分类名等于一级标题并且是第一次找到
                    if (sort.getName().equals(articleType.getMessage()) && flag) {
                        map.put(sort.getId(), sort);
                        i = 0;
                        flag = false;
                    }

                    if (map.size() > 0 && sort.getRely() != null && map.containsKey(sort.getRely()) &&
                            !map.containsKey(sort.getId())) {
                        map.put(sort.getId(), sort);
                    }

                }

            } while (count != 0);

            List<Sort> sorts = new ArrayList<>();
            Set<Integer> integers = map.keySet();

            for (Integer integer : integers) {
                Sort sort = map.get(integer);
                sorts.add(sort);
            }
            operations.set(key, sorts, 1, TimeUnit.HOURS);

            return sorts;
        }

        return null;
    }
}
