package cn.dlj1.auth;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.WebSessionManager;
import org.springframework.web.server.session.WebSessionStore;

import java.time.Duration;

/**
 * Auth整体配置
 *
 * @author fivewords
 * @date 2019/7/1 16:21
 */
@Configuration
public class Config {

    @Component
    @ConfigurationProperties(prefix = "auth")
    public class Props {
        private String cookieName = "GATEWAY-SESSION";
        private Duration cookieMaxAge = Duration.ofHours(24);

        private String redisHost;
        private int redisPort = 6379;
        private String redisPassword;
        private int redisDb = 0;

        public String getCookieName() {
            return cookieName;
        }

        public void setCookieName(String cookieName) {
            this.cookieName = cookieName;
        }

        public Duration getCookieMaxAge() {
            return cookieMaxAge;
        }

        public void setCookieMaxAge(Duration cookieMaxAge) {
            this.cookieMaxAge = cookieMaxAge;
        }

        public String getRedisHost() {
            return redisHost;
        }

        public void setRedisHost(String redisHost) {
            this.redisHost = redisHost;
        }

        public int getRedisPort() {
            return redisPort;
        }

        public void setRedisPort(int redisPort) {
            this.redisPort = redisPort;
        }

        public String getRedisPassword() {
            return redisPassword;
        }

        public void setRedisPassword(String redisPassword) {
            this.redisPassword = redisPassword;
        }

        public int getRedisDb() {
            return redisDb;
        }

        public void setRedisDb(int redisDb) {
            this.redisDb = redisDb;
        }
    }

    /**
     * 会话管理器
     *
     * @param webSessionStore
     * @param props
     * @return
     */
    @Bean
    public WebSessionManager webSessionManager(WebSessionStore webSessionStore, Props props) {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionStore(webSessionStore);

        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName(props.getCookieName());
        resolver.setCookieMaxAge(props.getCookieMaxAge());

        defaultWebSessionManager.setSessionIdResolver(resolver);
        return defaultWebSessionManager;
    }

    /**
     * 用于redis存储的连接池
     *
     * @param props
     * @return
     */
//    @Bean("sessionRedisFactory")
//    public ReactiveRedisConnectionFactory factory(Props props) {
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(props.getRedisHost());
//        configuration.setPort(props.getRedisPort());
//        configuration.setPassword(props.getRedisPassword());
//        configuration.setDatabase(props.getRedisDb());
//        return new LettuceConnectionFactory(configuration);
//    }
//
//    @Primary
//    @Bean
//    public ReactiveRedisConnectionFactory primaryFactory(Props props) {
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(props.getRedisHost());
//        configuration.setPort(props.getRedisPort());
//        configuration.setPassword(props.getRedisPassword());
//        configuration.setDatabase(0);
//        return new LettuceConnectionFactory(configuration);
//    }

    /**
     * redis存储
     *
     * @return
     */
//    @Order(5555)
//    @Bean("sessionRedisTemplate")
//    public ReactiveStringRedisTemplate redisTemplate(@Qualifier("sessionRedisFactory") ReactiveRedisConnectionFactory factory) {
//        ReactiveStringRedisTemplate template = new ReactiveStringRedisTemplate(factory);
//
//        return template;
//    }

}
