package cn.dlj1.gateway.filters;

import cn.dlj1.auth.AuthCheckService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final Log log = LogFactory.getLog(GatewayFilter.class);

    @Autowired
    AuthCheckService authCheckService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return authCheckService.check(exchange)
                .flatMap(integer -> {
                    if (integer == -1) {
                        try {
                            DataBuffer wrap = exchange.getResponse().bufferFactory().wrap(new String("你好").getBytes("UTF-8"));
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            exchange.getResponse().getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().writeWith(Mono.just(wrap));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    if (integer == 0) {

                    }
                    if (integer == 0) {

                    }

                    return chain.filter(exchange);
                });

//        mono.subscribe();

//        ServerWebExchange finalExchange = exchange;
//
//        mono.subscribe(integer -> {
//            ServerHttpResponse response = finalExchange.getResponse();
//            if (integer == -1) {
//                // 没登陆
//                response.setStatusCode(HttpStatus.FORBIDDEN);
//                response.setComplete();
//            } else if (integer == 0) {
//                response.setStatusCode(HttpStatus.FORBIDDEN);
//                response.setComplete();
//            } else {
//                chain.filter(finalExchange);
//            }
//            return;
//
//        });


    }

    @Override
    public int getOrder() {
        return 1;
    }


}