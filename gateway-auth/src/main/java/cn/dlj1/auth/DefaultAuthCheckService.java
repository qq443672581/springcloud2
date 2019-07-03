package cn.dlj1.auth;

import cn.dlj1.auth.session.DefaultWebSession;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DefaultAuthCheckService implements AuthCheckService {

    AntPathMatcher matcher = new AntPathMatcher();
    Map<String, String> patterns = new HashMap<>();

    @Override
    public Mono<Integer> check(ServerWebExchange exchange) {
        return exchange.getSession().flatMap(webSession -> {

            DefaultWebSession defaultWebSession = (DefaultWebSession) webSession;
            UserDetail user = defaultWebSession.getUser();
            if (null == user) {
                return Mono.just(-1);
            }

            List<AuthCode> authCodes = user.getAuthCodes();
            if (authCodes.size() == 0) {
                return Mono.just(0);
            }

            return Mono.just(1);
        });

    }

}
