package cn.pzhu.forum.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration //该注解使得该类变成与applicationContext.xml等同的类
public class ConfigBean {

    /**
     * 开启负载均很
     *
     * @return RestTemplate
     */
    @Bean
    //@LoadBalanced   // 开启负载均衡
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();

        List<MediaType> mediaTypes = new ArrayList<>();

        mediaTypes.add(MediaType.TEXT_PLAIN);
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.APPLICATION_JSON);
        httpMessageConverter.setSupportedMediaTypes(mediaTypes);

        restTemplate.getMessageConverters().add(httpMessageConverter);

        return restTemplate;
    }

    /*
     * 将Service的选择机制改为我们自己选择的选择机制
     */
    /*@Bean
    public IRule rule() {
        return new RandomRule();
    }*/


}