package cn.dlj1.lock.jedis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LockAutoConfiguration {

    @Bean
    public Aop aop(){
        return new Aop();
    }

}
