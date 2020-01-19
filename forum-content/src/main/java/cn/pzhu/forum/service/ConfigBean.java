package cn.pzhu.forum.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration //该注解使得该类变成与applicationContext.xml等同的类
public class ConfigBean {

    /**
     * 配置RestTemplate
     *
     * @return RestTemplate
     */
    @Bean
    //@LoadBalanced   // 开启负载均衡
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /*
     * 将Service的选择机制改为我们自己选择的选择机制
     */
   /* @Bean
    public IRule rule(){
        return new RandomRule();
    }*/

}