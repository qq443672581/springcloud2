package cn.dlj1.auth;

import cn.dlj1.auth.session.DefaultWebSession;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class DefaultAuthCheckService implements AuthCheckService {

    AntPathMatcher matcher = new AntPathMatcher();
    Map<String, String> patterns = new HashMap<>();

    @Override
    public Mono<Integer> check(ServerWebExchange exchange) {

        Mono<WebSession> session = exchange.getSession();
        DefaultWebSession block = (DefaultWebSession) session.block();

        if (!block.isLogin()) {
            return Mono.just(-1);
        }
        Set<String> authCodes = block.getAuthCodes();
        if (null == authCodes) {
            return Mono.just(0);
        }

        RequestPath path = exchange.getRequest().getPath();
        String value = path.value();

        String code = null;
        for (Map.Entry<String, String> pattern : patterns.entrySet()) {
            boolean match = matcher.match(pattern.getKey(), value);
            if (match) {
                code = pattern.getValue();
                break;
            }
        }
        // 地址不可用
        if (null == code) {
            return Mono.just(0);
        }
        // 无权限
        if (!authCodes.contains(code)) {
            return Mono.just(0);
        }


        return Mono.just(1);
    }

}
