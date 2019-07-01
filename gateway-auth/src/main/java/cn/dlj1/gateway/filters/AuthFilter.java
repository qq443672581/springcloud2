package cn.dlj1.gateway.filters;

import cn.dlj1.auth.AuthCheckService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final Log log = LogFactory.getLog(GatewayFilter.class);

    @Autowired
    AuthCheckService authCheckService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Mono<Integer> check = authCheckService.check(exchange);

        Integer block = check.block();
        if (block == -1) {
            // 没登陆
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return Mono.empty();
        }
        if (block == 0) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return Mono.empty();
        }

        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return 1;
    }
}