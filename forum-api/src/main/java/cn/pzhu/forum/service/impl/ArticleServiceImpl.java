package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.content.ArticleType;
import cn.pzhu.forum.content.RedisKeyConstant;
import cn.pzhu.forum.content.TopFlag;
import cn.pzhu.forum.dao.ArticleDao;
import cn.pzhu.forum.dao.RecordDao;
import cn.pzhu.forum.dao.SortDao;
import cn.pzhu.forum.entity.Article;
import cn.pzhu.forum.entity.Record;
import cn.pzhu.forum.entity.Sort;
import cn.pzhu.forum.service.ArticleService;
import cn.pzhu.forum.service.SortService;
import cn.pzhu.forum.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * 博客信息管理类
 */
@Service
@Slf4j
@SuppressWarnings("unchecked")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private SortDao sortDao;

    @Autowired
    private SortService sortService;

    @Autowired
    private RecordDao recordDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public String add(Article article) {

        log.info("cn.pzhu.forum.service.impl.ArticleServiceImpl.add-添加博客");

        article.setTime(Utils.getDate(true));
        String substring = UUID.randomUUID().toString().substring(0, 8);
        article.setPrincipal(substring); // 设置唯一的编号

        Integer num = articleDao.add(article);

        // 插入成功
        if (num > 0) {

            boolean flag = sortDao.addSortNumber(article.getSortId());

            // 修改失败
            if (!flag) {
                return "";
            }

            //将文章加入到该用户的文章缓存列表中
            String userKey = RedisKeyConstant.USER_ARTICLE_LIST + article.getUserName();

            SetOperations operations = redisTemplate.opsForSet();
            redisTemplate.expire(userKey, 1, TimeUnit.HOURS);// 设置key过期时间
            operations.add(userKey, article);

            // 将文章添加到指定分类下的缓存列表中
            String sort = RedisKeyConstant.SORT_ARTICLE_LIST + article.getSortId();
            operations.add(sort, article);
            redisTemplate.expire(sort, 1, TimeUnit.HOURS);

            // 将文章添加到所有文章的缓存列表中
            String list = RedisKeyConstant.ARTICLE_LIST;
            operations.add(list, article);
            redisTemplate.expire(list, 1, TimeUnit.HOURS);

            return article.getPrincipal();
        }

        return "";
    }

    @Override
    public Article get(Integer id) {

        log.info("cn.pzhu.forum.service.impl.ArticleServiceImpl.get-查询博客-入参：id = {}", id);

        Article article = null;

        String key = RedisKeyConstant.ARTICLE_LIST;
        Boolean hasKey = redisTemplate.hasKey(key);
        SetOperations operations = redisTemplate.opsForSet();

        if (hasKey != null && hasKey) {
            Set<Article> members = operations.members(key);

            Stream<Article> limit = members.stream().filter((x) -> x.getId().equals(id)).limit(1);

            Optional<Article> first = limit.findFirst();

            if (first.isPresent()) {
                article = first.get();
            }

        } else {
            article = articleDao.get(id);
        }

        log.info("cn.pzhu.forum.service.impl.ArticleServiceImpl.get-查询博客-结果：{}", article);
        return article;
    }

    @Override
    public List<Article> list() {

        String key = RedisKeyConstant.ARTICLE_LIST;

        Boolean hasKey = redisTemplate.hasKey(key);
        SetOperations operations = redisTemplate.opsForSet();

        if (hasKey != null && hasKey) {
            Set<Article> members = operations.members(key);

            return new ArrayList<>(members);
        }

        List<Article> list = articleDao.list();
        if (list != null) {
            for (Article article : list) {
                operations.add(key, article);
            }

        }
        redisTemplate.expire(key, 1, TimeUnit.HOURS);

        return list;
    }

    @Override
    public List<Article> list(Integer sortId, int number) {
        log.info("cn.pzhu.forum.service.impl.ArticleServiceImpl.list(java.lang.Integer, int)-根据分类ID查询-" +
                "入参：分类ID：{},查询数量:{}", sortId, number);

        String key = RedisKeyConstant.SORT_ARTICLE_LIST + sortId;
        String articleList = RedisKeyConstant.ARTICLE_LIST;

        redisTemplate.expire(key, 1, TimeUnit.HOURS);
        redisTemplate.expire(articleList, 1, TimeUnit.HOURS);

        SetOperations operations = redisTemplate.opsForSet();
        Boolean hasKey = redisTemplate.hasKey(key);

        if (hasKey != null && hasKey) {

            log.info("从缓存中查询");

            Set<Article> members = operations.members(key);

            return sortAntConvert(members, number);
        }

        hasKey = redisTemplate.hasKey(articleList);

        Collection<Article> list;

        if (hasKey != null && hasKey) {
            log.info("从缓存中查询文章");
            list = operations.members(articleList);

        } else {
            log.info("从数据库中查询对应分类的文章");

            list = articleDao.sortArticleList(sortId);
        }

        List<Article> sorts = splitWithSort(list, sortId, number);

        assert sorts != null;
        for (Article sort : sorts) {
            operations.add(key, sort);
        }


        return sorts;
    }

    @Override
    public List<Article> list(ArticleType articleType, int number) {

        log.info("根据大的分类信息查询博客");

        List<Article> list = articlesList(articleType, number);

        log.info("查询结果：查询结果是否为空：{}", list == null);

        return list;
    }

    @Override
    public List<Article> userList(String userName) {
        log.info("cn.pzhu.forum.service.impl.ArticleServiceImpl.userList-根据用户名查询用户已发表博客-入参：" +
                "userName = {}", userName);

        String key = RedisKeyConstant.USER_ARTICLE_LIST + userName;
        SetOperations operations = redisTemplate.opsForSet();

        Boolean hasKey = redisTemplate.hasKey(key);

        List<Article> list = new ArrayList<>();

        boolean flag = false;

        if (hasKey != null && hasKey) {
            log.info("从缓存中查询");

            Set<Article> members = operations.members(key);

            if (members.size() <= 2) {
                flag = true;
            } else {
                members.stream().sorted((x, y) -> x.getTop() > y.getTop() ? 1 : 0).forEach(list::add);
            }


        }

        if (flag || (hasKey == null || !hasKey)) {
            log.info("从数据库中查询");

            List<Article> articles = articleDao.userArticleList(userName);

            articles.stream().sorted((x, y) -> x.getTop() > y.getTop() ? 1 : 0).forEach((x) -> {
                list.add(x);
                operations.add(key, x);
            });
            redisTemplate.expire(key, 1, TimeUnit.HOURS);
        }

        return list;
    }

    @Override
    public List<Article> topList(ArticleType articleType, int number) {

        log.info("根据大的类别查询置顶博客列表");

        List<Article> list = articlesList(articleType, number);

        List<Article> articles = new ArrayList<>();

        list.stream().filter((x) -> x.getTop().equals(TopFlag.TOP.getFlag())).forEach(articles::add);

        log.info("查询结果：博客数量 = {}", articles.size());
        return articles;
    }

    @Override
    public List<Article> list(String key) {

        String keys = RedisKeyConstant.ARTICLE_LIST_KEY + key;
        Boolean hasKey = redisTemplate.hasKey(keys);
        ValueOperations operations = redisTemplate.opsForValue();

        if (hasKey != null && hasKey) {

            return (List<Article>) operations.get(keys);
        }

        key = "%" + key + "%";

        List<Article> list = articleDao.keyList(key);

        if (list != null) {
            operations.set(keys, list, 1, TimeUnit.HOURS);
        }

        redisTemplate.expire(keys, 1, TimeUnit.HOURS);

        return list;
    }


    @Override
    public boolean update(Article article) {

        boolean update = articleDao.update(article);

        if (update) {

            Article article1 = articleDao.get(article.getId());

            //让缓存过期
            String userKey = RedisKeyConstant.USER_ARTICLE_LIST + article1.getUserName();

            // 设置key过期时间
            redisTemplate.expire(userKey, 1, TimeUnit.SECONDS);

            String sort = RedisKeyConstant.SORT_ARTICLE_LIST + article1.getSortId();
            redisTemplate.expire(sort, 1, TimeUnit.SECONDS);

            String list = RedisKeyConstant.ARTICLE_LIST;
            redisTemplate.expire(list, 1, TimeUnit.SECONDS);
        }


        return update;
    }

    @Override
    public boolean delete(Integer id) {
        log.info("cn.pzhu.forum.service.impl.ArticleServiceImpl.delete-删除指定的博客-入参：{}", id);

        Article article = get(id);

        boolean delete = articleDao.delete(id);

        if (delete) {

            SetOperations operations = redisTemplate.opsForSet();
            String key = RedisKeyConstant.ARTICLE_LIST;
            Boolean hasKey = redisTemplate.hasKey(key);

            if (hasKey != null && hasKey) {

                // 移除博客列表里面的数据
                operations.remove(key, article);
            }

            String s = RedisKeyConstant.SORT_ARTICLE_LIST + article.getSortId();
            Boolean hasKey1 = redisTemplate.hasKey(s);
            if (hasKey1 != null && hasKey1) {

                // 移除分类博客列表里面的数据
                operations.remove(s, article);
            }

            String userKey = RedisKeyConstant.USER_ARTICLE_LIST + article.getUserName();
            Boolean hasKey2 = redisTemplate.hasKey(userKey);

            if (hasKey2 != null && hasKey2) {
                operations.remove(userKey, article);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean setTop(Integer id) {

        boolean b = articleDao.setTop(id);

        if (b) {
            String key = RedisKeyConstant.ARTICLE_TOP_LIST;
            SetOperations operations = redisTemplate.opsForSet();

            Article article = articleDao.get(id);

            operations.add(key, article);

            return true;
        }

        return false;
    }

    @Transactional
    @Override
    public Article get(String userName, String principal) {

        log.info("cn.pzhu.forum.service.impl.ArticleServiceImpl.get(java.lang.String, java.lang.String)-" +
                "根据用户名和文章关键字查询文章信息-入参：userName = {},principal = {}", userName, principal);

        String userKey = RedisKeyConstant.USER_ARTICLE_LIST + userName;

        Boolean hasKey = redisTemplate.hasKey(userKey);
        SetOperations operations = redisTemplate.opsForSet();
        Article article = null;

        if (hasKey != null && hasKey) {
            Set<Article> members = operations.members(userKey);

            Stream<Article> limit = members.stream().filter((x) -> x.getPrincipal().equals(principal) && x.getUserName().equals(userName))
                    .limit(1);

            Optional<Article> first = limit.findFirst();

            if (first.isPresent()) {
                article = first.get();
            }

        }

        if (article == null) {
            article = articleDao.getByPrincipal(userName, principal);

            if (article != null) {
                operations.add(userKey, article);
            }
        }

        if (article != null) {
            boolean b = articleDao.uploadReadNumber(article.getId());

            if (!b) {
                throw new RuntimeException("更新数据库异常");
            }
        }

        redisTemplate.expire(userKey, 1, TimeUnit.HOURS);
        log.info("cn.pzhu.forum.service.impl.ArticleServiceImpl.get(java.lang.String, java.lang.String)-" +
                "根据用户名和文章关键字查询文章信息-返回值：article是否为空 {}", article == null);

        return article;
    }

    @Override
    public Integer like(String userName, String principal) {

        Record record = new Record(0, userName, principal);

        boolean add = recordDao.add(record);

        if (add) {

            String key = RedisKeyConstant.ARTICLE_RECORD_LIST_COUNT + principal;

            ValueOperations operations = redisTemplate.opsForValue();
            Integer o = (Integer) operations.get(key);
            operations.set(key, o + 1, 1, TimeUnit.DAYS);

            String flag = RedisKeyConstant.USER_ARTICLE_RECORD_FLAG + userName + principal;
            operations.set(flag, record);

            return o + 1;
        }

        return -1;
    }

    @Override
    public Integer likeCount(String principal) {

        String key = RedisKeyConstant.ARTICLE_RECORD_LIST_COUNT + principal;

        Boolean hasKey = redisTemplate.hasKey(key);
        ValueOperations operations = redisTemplate.opsForValue();

        if (hasKey != null && hasKey) {

            return (Integer) operations.get(key);
        }

        Integer list = recordDao.list(principal);

        operations.set(key, list, 1, TimeUnit.DAYS);

        return list;
    }

    @Override
    public boolean hashLiked(String userName, String principal) {

        String flag = RedisKeyConstant.USER_ARTICLE_RECORD_FLAG + userName + principal;
        Boolean hasKey = redisTemplate.hasKey(flag);

        return hasKey != null && hasKey;
    }

    @Override
    public List<Article> similar(String title, int number) {

        log.info("查询相似的文章");

        String articleKey = RedisKeyConstant.SIMILAR_ARTICLE_LIST + title;

        Boolean hasKey1 = redisTemplate.hasKey(articleKey);
        ValueOperations operations1 = redisTemplate.opsForValue();
        if (hasKey1) {
            log.info("从缓存中查询相似的文章");
            return (List<Article>) operations1.get(articleKey);
        }

        String key = RedisKeyConstant.ARTICLE_LIST;

        Boolean hasKey = redisTemplate.hasKey(key);
        SetOperations operations = redisTemplate.opsForSet();
        List<Article> list = new ArrayList<>();

        if (hasKey != null && hasKey) {

            Set<Article> members = operations.members(key);

            members.stream().filter((x) -> {

                float similarityRatio = getSimilarityRatio(title, x.getTitle());

                return similarityRatio > 0.1;
            }).limit(number).forEach(list::add);

            if (list.size() == 0) {
                members.stream().limit(number).forEach(list::add);
            } else {
                operations1.set(articleKey, list, 1, TimeUnit.DAYS);
            }

            return list;

        }

        List<Article> articles = articleDao.list();

        articles.stream().filter((x) -> {

            float similarityRatio = getSimilarityRatio(title, x.getTitle());

            return similarityRatio > 0.1;
        }).limit(number).forEach(list::add);

        if (list.size() == 0) {
            articles.stream().limit(number).forEach(list::add);
        } else {
            operations1.set(articleKey, list, 1, TimeUnit.DAYS);
        }
        return list;
    }

    @Override
    public int articleCount() {
        // 通过文章列表的个数统计文章总数
        return list().size();
    }

    @Override
    public int recordCount() {

        return recordDao.recordCount();
    }

    /**
     * 将传入的集合转换为List,并且对list进行排序（根据是否置顶，和阅读量）
     *
     * @param source 传入的数据
     * @param number 返回集合中元素的个数
     * @return 返回排好序的list
     */
    private List<Article> sortAntConvert(Collection<Article> source, int number) {

        Comparator<Article> comparator = (o1, o2) -> {

            if (!o1.getTop().equals(o2.getTop())) {
                return o1.getTop() > o2.getTop() ? 1 : 0;
            }

            return o1.getReadNumber() > o2.getReadNumber() ? 1 : 0;
        };

        List<Article> list = new ArrayList<>();
        source.stream().limit(number).sorted(comparator).forEach(list::add);

        return list;
    }

    /**
     * 将传入的数据根据分类进行分割成每个不同的模块
     *
     * @param source 原数据集合
     * @param sortId 分类ID
     * @param number 一次性显示的数量
     * @return 分割后的数据集合
     */
    private List<Article> splitWithSort(Collection<Article> source, Integer sortId, Integer number) {

        if (source == null) {
            return null;
        }

        List<Article> list = new ArrayList<>();

        for (Article article : source) {

            if (article.getSort() != null && article.getSort().getId().equals(sortId)) {
                list.add(article);
                number--;
            } else if (article.getSortId() != null && article.getSortId().equals(sortId)) {
                list.add(article);
                number--;
            }

            if (number == 0) {
                break;
            }

        }

        return list;
    }


    /**
     * 查询对应分类下的置顶的前number条信息
     *
     * @param articleType 查询类型
     * @param number      置顶条数
     * @return 博客集合
     */
    private List<Article> articlesList(ArticleType articleType, int number) {

        List<Sort> list = sortService.list();

        List<Integer> idList = new ArrayList<>();

        boolean flag = true;  // 表示是否找到一级标题

        int count = 3;  // 用于判断循环次数，避免出现分类信息丢失的情况

        do {
            count--;
            for (int i = 0; i < list.size(); i++) {

                Sort sort = list.get(i);

                if (sort.getName().equals(articleType.getMessage()) && flag) {  // 分类名等于一级标题并且是第一次找到

                    idList.add(sort.getId());
                    i = 0;
                    flag = false;

                }

                // 当前ID的集合中存在有数据，并且这个分类ID的上级标题存在这个里面并且是第一次加入
                if (idList.size() > 0 && idList.contains(sort.getRely()) && !idList.contains(sort.getId())) {
                    idList.add(sort.getId());
                }
            }

        } while (count != 0);

        List<Article> articleList = new ArrayList<>();

        for (Integer integer : idList) {

            List<Article> list1 = list(integer, number);

            articleList.addAll(list1);
        }

        return articleList;
    }

    /**
     * 比较两个字符串的相识度
     * 核心算法：用一个二维数组记录每个字符串是否相同，如果相同记为0，不相同记为1，每行每列相同个数累加
     * 则数组最后一个数为不相同的总数，从而判断这两个字符的相识度
     * <p>
     * 设 s 的长度为 n，t 的长度为 m。如果 n = 0，则返回 m 并退出；如果 m=0，则返回 n 并退出。否则构建一个数组 d[0..m, 0..n]。
     * 将第0行初始化为 0..n，第0列初始化为0..m。
     * 依次检查 s 的每个字母(i=1..n)。
     * 依次检查 t 的每个字母(j=1..m)。
     * 如果 s[i]=t[j]，则 cost=0；如果 s[i]!=t[j]，则 cost=1。将 d[i,j] 设置为以下三个值中的最小值：
     * 紧邻当前格上方的格的值加一，即 d[i-1,j]+1
     * 紧邻当前格左方的格的值加一，即 d[i,j-1]+1
     * 当前格左上方的格的值加cost，即 d[i-1,j-1]+cost
     * 重复3-6步直到循环结束。d[n,m]即为莱茵斯坦距离。
     *
     * @param str    源字符串
     * @param target 目标字符串
     * @return 相似度
     */
    private int compare(String str, String target) {
        int[][] d;              // 矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        // 初始化第一列
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        // 初始化第一行
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) {
            // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }


    /**
     * 获取最小的值（比较三个数的大小）
     *
     * @param one   第一个数
     * @param two   第二个数
     * @param three 第三个数
     * @return 最小值
     */
    private int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 获取两字符串的相似度
     *
     * @param str    源字符串
     * @param target 目标字符串
     * @return 相似度（0-1）
     */
    private float getSimilarityRatio(String str, String target) {
        int max = Math.max(str.length(), target.length());
        return 1 - (float) compare(str, target) / max;
    }


}
