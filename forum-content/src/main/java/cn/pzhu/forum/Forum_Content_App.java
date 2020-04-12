package cn.pzhu.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author impassivey
 */
@SpringBootApplication
public class Forum_Content_App {

    public static void main(String[] args) {
        SpringApplication.run(Forum_Content_App.class, args);
    }
}
