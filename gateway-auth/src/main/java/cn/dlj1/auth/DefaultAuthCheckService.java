package cn.dlj1.auth;

import cn.dlj1.auth.session.DefaultWebSession;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DefaultAuthCheckService implements AuthCheckService {

    AntPathMatcher matcher = new AntPathMatcher();
    Map<String, String> patterns = new HashMap<>();

    @PostConstruct
    public void init() {
        patterns.put("/book/**", "book");
    }

    @Override
    public Mono<Integer> check(ServerWebExchange exchange) {
        return exchange.getSession().flatMap(webSession -> {

            DefaultWebSession defaultWebSession = (DefaultWebSession) webSession;
            UserDetail user = defaultWebSession.getUser();
            if (null == user) {
                return Mono.just(-1);
            }

            // 没有权限
            Set<String> authCodes = user.getAuthCodes();
            if (null == authCodes || authCodes.size() == 0) {
                return Mono.just(0);
            }

            String path = exchange.getRequest().getPath().value();

            for (Map.Entry<String, String> entry : patterns.entrySet()) {
                if (!matcher.match(entry.getKey(), path)) {
                    continue;
                }
                // 拥有权限
                if (authCodes.contains(entry.getValue())) {
                    return Mono.just(1);
                }
            }

            return Mono.just(0);
        });

    }

}
