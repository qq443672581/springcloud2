package cn.dlj1.lock.jedis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
public class BeanConfig {

    @ConfigurationProperties(prefix = "jedis")
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration(){
        return new RedisStandaloneConfiguration();
    }

    @Bean
    public JedisConnectionFactory factory(){
        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration());
        return factory;
    }

}
