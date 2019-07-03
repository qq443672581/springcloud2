package cn.dlj1.auth.session;

import cn.dlj1.auth.UserDetail;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * 默认session实现
 *
 * @author fivewords
 * @date 2019/7/1 15:37
 */
public class DefaultWebSession implements WebSession {

    // 创建时间
    public static String SA_CREATE_TIME = "SESSION_ATTRIBUTE:CREATE_TIME";
    // 最后访问时间
    static String SA_LAST_ACCESS_TIME = "SESSION_ATTRIBUTE:LAST_ACCESS_TIME";
    // 最大存活时间
    static String SA_MAX_IDLE_TIME = "SESSION_ATTRIBUTE:MAX_IDLE_TIME";
    // 是否登录
    static String SA_LOGIN = "SESSION_ATTRIBUTE:LOGIN";
    // 用户
    public static String SA_USER = "SESSION_ATTRIBUTE:USER";

    private String id;
    private boolean start = true;
    private Attributes attributes;
    private ReactiveRedisTemplate<String, Object> redisTemplate;

    public DefaultWebSession(String id, Attributes attributes, ReactiveRedisTemplate<String, Object> redisTemplate) {
        this.id = id;
        this.attributes = attributes;
        this.redisTemplate = redisTemplate;
    }

    public void init(long CookieMaxAge) {
        Date date = new Date();
        attributes.put(DefaultWebSession.SA_CREATE_TIME, date);
        attributes.put(DefaultWebSession.SA_LAST_ACCESS_TIME, date);
        attributes.put(DefaultWebSession.SA_MAX_IDLE_TIME, CookieMaxAge);
        attributes.put(DefaultWebSession.SA_LOGIN, false);
        attributes.put(DefaultWebSession.SA_USER, null);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public UserDetail getUser() {
        return (UserDetail) this.attributes.get(SA_USER);
    }

    public void setUser(UserDetail user) {
        this.attributes.put(SA_USER, user);
    }

    public boolean isLogin() {
        return null != getUser();
    }

    @Override
    public void start() {
        System.out.println("session:start");
    }

    @Override
    public boolean isStarted() {
        return start;
    }

    @Override
    public Mono<Void> changeSessionId() {
        System.out.println("session:changeSessionId");
        return Mono.empty();
    }

    @Override
    public Mono<Void> invalidate() {
        start = false;
        attributes.clear();
        System.out.println("session:invalidate");
        return this.redisTemplate.delete(id).flatMap(aLong -> Mono.empty());
    }

    @Override
    public Mono<Void> save() {
        Duration maxIdleTime = getMaxIdleTime();
        Map<String, Object> attributes = this.getAttributes();
        return this.redisTemplate.opsForValue().set(id, attributes, maxIdleTime).flatMap(aBoolean -> Mono.empty());

    }

    @Override
    public boolean isExpired() {
        Instant lastAccessTime = getLastAccessTime();
        Duration maxIdleTime = getMaxIdleTime();
        if (null == lastAccessTime || null == maxIdleTime || (lastAccessTime.toEpochMilli() + maxIdleTime.toMillis()) < System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    @Override
    public Instant getCreationTime() {
        Date date = getAttribute(SA_CREATE_TIME);
        if (null == date) {
            return null;
        }
        return date.toInstant();
    }

    @Override
    public Instant getLastAccessTime() {
        Date date = getAttribute(SA_CREATE_TIME);
        if (null == date) {
            return null;
        }
        return date.toInstant();
    }

    @Override
    public void setMaxIdleTime(Duration duration) {
        attributes.put(SA_MAX_IDLE_TIME, duration.toMillis());
    }

    @Override
    public Duration getMaxIdleTime() {
        Long d = getAttribute(SA_MAX_IDLE_TIME);
        if (null == d) {
            return null;
        }
        return Duration.ofMillis(d);
    }

    @Override
    public <T> T getAttribute(String name) {
        return (T) getAttributes().get(name);
    }

    @Override
    public <T> T getRequiredAttribute(String name) {
        T o = getAttribute(name);
        if (null == o) {
            throw new IllegalArgumentException();
        }
        return o;
    }

    @Override
    public <T> T getAttributeOrDefault(String name, T defaultValue) {
        T o = getAttribute(name);
        if (null == o) {
            return defaultValue;
        }
        return o;
    }
}
