package cn.dlj1.auth;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 权限处理
 */
public interface AuthCheckService {

    Mono<Integer> check(ServerWebExchange exchange);

}
