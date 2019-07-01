package cn.dlj1.auth.session;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebSession;
import org.springframework.web.server.session.WebSessionStore;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author fivewords
 * @date 2019/7/1 16:00
 */
@Component
public class RedisSessionStore implements WebSessionStore {

    @Resource(name = "sessionRedis")
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<WebSession> createWebSession() {
        DefaultWebSession session = new DefaultWebSession(UUID.randomUUID().toString(), new HashMap<>(), redisTemplate);
        return Mono.just(session);
    }

    @Override
    public Mono<WebSession> retrieveSession(String id) {
        HashMap o = (HashMap) redisTemplate.opsForValue().get(id);
        if (null == o) {
            return Mono.empty();
        }

        WebSession webSession = new DefaultWebSession(id, o, redisTemplate);
        return Mono.just(webSession);
    }

    @Override
    public Mono<Void> removeSession(String s) {
        redisTemplate.delete(s);
        return Mono.empty();
    }

    @Override
    public Mono<WebSession> updateLastAccessTime(WebSession webSession) {
        redisTemplate.opsForValue().set(webSession.getId(), webSession, Duration.ofHours(24));
        return Mono.just(webSession);
    }
}
