package cn.pzhu.forum.config;

import cn.pzhu.forum.content.URLContent;
import cn.pzhu.forum.realm.RetryLimitHashedCredentialsMatcher;
import cn.pzhu.forum.realm.ShiroLoginFilter;
import cn.pzhu.forum.realm.ShiroRealm;
import java.util.HashMap;
import java.util.Map;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Shiro 配置类
 *
 * @author impassivey
 */
@Configuration
public class ShiroConfig {

    private static final String CACHE_KEY = "shiro:cache:";
    private static final String SESSION_KEY = "shiro:session:";
    private static final String NAME = "custom.name";
    private static final String VALUE = "/";

    /**
     * Shiro 过滤器工厂
     *
     * @param securityManager Shiro安全管理器
     * @return shiro过滤工厂
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<>(4);
        map.put("/user/**", "user");
        map.put("/admin/**", "roles[admin]");
        map.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        // shiroFilterFactoryBean.setLoginUrl("http://localhost:8001/index.html");
        shiroFilterFactoryBean.setUnauthorizedUrl(URLContent.LOGIN);

        return shiroFilterFactoryBean;
    }

    @Bean
    public ShiroLoginFilter shiroLoginFilter() {
        return new ShiroLoginFilter();
    }

    /**
     * 配置Shiro安全管理器
     *
     * @param shiroRealm     用户自定义Realm
     * @param sessionManager Session管理器
     * @param cacheManager   缓存管理器
     * @return 安全管理器实体
     */
    @Bean
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm,
                                                     DefaultWebSessionManager sessionManager,
                                                     RedisCacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }

    /**
     * 配置Redis连接池
     *
     * @param redisProperties Redis的配置信息
     * @return Redis连接池
     */
    @Bean
    public JedisPool jedisPool(RedisProperties redisProperties) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        return new JedisPool(poolConfig, redisProperties.getHost(), redisProperties.getPort(), 100000);
    }

    /**
     * 配置Redis管理器
     *
     * @param jedisPool Java访问Redis的连接池
     * @return Redis管理器
     */
    @Bean
    public RedisManager redisManager(JedisPool jedisPool) {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("127.0.0.1");
        redisManager.setJedisPool(jedisPool);
        return redisManager;
    }

    /**
     * 配置Redis缓存管理器
     *
     * @param redisManager Redis管理器
     * @return Redis缓存管理器
     */
    @Bean
    public RedisCacheManager cacheManager(RedisManager redisManager) {
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisManager);
        cacheManager.setKeyPrefix(CACHE_KEY);
        return cacheManager;
    }

    /**
     * Redis 访问Session的DAO层
     *
     * @param redisManager Redis管理器
     * @return Session实体
     */
    @Bean
    public RedisSessionDAO redisSessionDao(RedisManager redisManager) {
        RedisSessionDAO redisSessionDao = new RedisSessionDAO();
        redisSessionDao.setKeyPrefix(SESSION_KEY);
        redisSessionDao.setRedisManager(redisManager);
        return redisSessionDao;
    }

    @Bean
    public DefaultWebSessionManager sessionManager(RedisSessionDAO redisSessionDAO, SimpleCookie simpleCookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(simpleCookie);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    @Bean
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName(NAME);
        simpleCookie.setValue(VALUE);
        return simpleCookie;
    }

    @Bean
    public ShiroRealm shiroRealm(RedisCacheManager redisCacheManager, RetryLimitHashedCredentialsMatcher credentialsMatcher) {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCacheManager(redisCacheManager);
        shiroRealm.setAuthenticationCachingEnabled(true);
        shiroRealm.setAuthorizationCachingEnabled(true);
        shiroRealm.setCredentialsMatcher(credentialsMatcher);
        return shiroRealm;
    }

    /**
     * 用于密码检验
     *
     * @return 密码检验器
     */
    @Bean
    public RetryLimitHashedCredentialsMatcher credentialsMatcher() {
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);
        return credentialsMatcher;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor advisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {

        return new LifecycleBeanPostProcessor();
    }

}
