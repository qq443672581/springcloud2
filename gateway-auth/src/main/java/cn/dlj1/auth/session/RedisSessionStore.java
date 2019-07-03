package cn.dlj1.auth.session;

import cn.dlj1.auth.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebSession;
import org.springframework.web.server.session.WebSessionStore;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author fivewords
 * @date 2019/7/1 16:00
 */
@Component
public class RedisSessionStore implements WebSessionStore {

    //    @Resource(name = "sessionRedisTemplate")
    @Resource(name = "reactiveRedisTemplate")
    ReactiveRedisTemplate redisTemplate;
    @Autowired
    Config.Props props;

    @Override
    public Mono<WebSession> createWebSession() {
        String sessionId = UUID.randomUUID().toString();
        Attributes attributes = new Attributes();
        DefaultWebSession session = new DefaultWebSession(sessionId, attributes, redisTemplate);
        session.init(props.getCookieMaxAge().toMillis());

        return Mono.just(session);
    }

    @Override
    public Mono<WebSession> retrieveSession(String id) {
        return redisTemplate
                .opsForValue()
                .get(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(s -> {
                    Attributes attributes = (Attributes) s;
                    return Mono.just(new DefaultWebSession(id, attributes, redisTemplate));
                });
    }

    @Override
    public Mono<Void> removeSession(String s) {
        return redisTemplate.delete(s);
    }

    @Override
    public Mono<WebSession> updateLastAccessTime(WebSession webSession) {
        return Mono.just(webSession);
    }
}
