package cn.dlj1.auth.session;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author fivewords
 * @date 2019/7/1 15:37
 */
public class DefaultWebSession implements WebSession {

    // 最后访问时间
    static String SA_LAST_ACCESS_TIME = "SESSION_ATTRIBUTE:LAST_ACCESS_TIME";
    // 是否登录
    static String SA_LOGIN = "SESSION_ATTRIBUTE:LOGIN";
    // 用户
    static String SA_USER = "SESSION_ATTRIBUTE:USER";
    // 权限
    static String SA_AUTH_CODES = "SESSION_ATTRIBUTE:AUTH_CODES";

    private String id;
    private Map<String, Object> attributes;
    private RedisTemplate<String, Object> redisTemplate;

    public DefaultWebSession(String id, Map<String, Object> attributes, RedisTemplate<String, Object> redisTemplate) {
        this.id = id;
        this.attributes = attributes;
        this.redisTemplate = redisTemplate;

        this.attributes.put(SA_LAST_ACCESS_TIME, new Date());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Set<String> getAuthCodes() {
        return (HashSet<String>) this.attributes.get(SA_AUTH_CODES);
    }

    public Object getUser() {
        return this.attributes.get(SA_USER);
    }

    public boolean isLogin() {
        Boolean login = (Boolean) this.attributes.get(SA_LOGIN);
        return null != login && login;
    }

    @Override
    public void start() {
        System.out.println("session:start");
    }

    @Override
    public boolean isStarted() {
        System.out.println("session:isStarted");
        return true;
    }

    @Override
    public Mono<Void> changeSessionId() {
        System.out.println("session:changeSessionId");
        return null;
    }

    @Override
    public Mono<Void> invalidate() {
        System.out.println("session:invalidate");
        return null;
    }

    @Override
    public Mono<Void> save() {
        this.redisTemplate.opsForValue().set(id, getAttributes(), Duration.ofHours(24));
        return Mono.empty();
    }

    @Override
    public boolean isExpired() {
        System.out.println("session:isExpired");
        return false;
    }

    @Override
    public Instant getCreationTime() {
        return null;
    }

    @Override
    public Instant getLastAccessTime() {
        return null;
    }

    @Override
    public void setMaxIdleTime(Duration duration) {

    }

    @Override
    public Duration getMaxIdleTime() {
        return null;
    }

    @Override
    public <T> T getAttribute(String name) {
        return (T) getAttributes().get(name);
    }

    @Override
    public <T> T getRequiredAttribute(String name) {
        throw new IllegalArgumentException();
    }

    @Override
    public <T> T getAttributeOrDefault(String name, T defaultValue) {
        return defaultValue;
    }
}
