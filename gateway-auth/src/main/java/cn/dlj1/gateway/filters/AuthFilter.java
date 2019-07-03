package cn.dlj1.gateway.filters;

import cn.dlj1.auth.AuthCheckService;
import cn.dlj1.auth.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    AuthCheckService authCheckService;
    @Autowired
    Config.Props props;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return authCheckService.check(exchange)
                .flatMap(integer -> {
                    if (integer == -1 || integer == 0) {
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.SEE_OTHER);
                        response.getHeaders().set("Location", integer == -1 ? props.getLoginHtml() : props.getNoAccessHtml());
                        return response.setComplete();
                    }

                    return chain.filter(exchange);
                });
    }

    @Override
    public int getOrder() {
        return 1;
    }


}