package cn.dlj1.auth.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.WebSessionManager;
import org.springframework.web.server.session.WebSessionStore;

import java.time.Duration;

/**
 * @author fivewords
 * @date 2019/7/1 16:21
 */
@Configuration
public class Config {

    @Bean
    public WebSessionManager webSessionManager(WebSessionStore webSessionStore) {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionStore(webSessionStore);

        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName("GATEWAY-SESSION");
        resolver.setCookieMaxAge(Duration.ofHours(24));

        defaultWebSessionManager.setSessionIdResolver(resolver);
        return defaultWebSessionManager;
    }

    @Bean("sessionRedis")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("10.16.0.58");
        redisStandaloneConfiguration.setPassword("123456");
        redisStandaloneConfiguration.setDatabase(3);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        factory.afterPropertiesSet();

        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setConnectionFactory(factory);

        template.afterPropertiesSet();
        return template;
    }

}
