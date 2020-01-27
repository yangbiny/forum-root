package cn.pzhu.forum.biz.files;

import javax.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author impassivey
 */
@ComponentScan
@Configuration
public class FileConfig {

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize("50MB");
    factory.setMaxRequestSize("50MB");
    return factory.createMultipartConfig();
  }

}
