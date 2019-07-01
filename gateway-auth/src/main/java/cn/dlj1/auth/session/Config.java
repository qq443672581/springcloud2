package cn.dlj1.auth.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.WebSessionManager;
import org.springframework.web.server.session.WebSessionStore;

import java.time.Duration;
import java.util.HashMap;

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
    public RedisTemplate<String, Object> redisTemplate(@Value("${spring.redis.host}") String host,
                                                       @Value("${spring.redis.password}") String password) {

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setDatabase(3);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        factory.afterPropertiesSet();

        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(HashMap.class));
        template.setConnectionFactory(factory);

        template.afterPropertiesSet();
        return template;
    }

}
