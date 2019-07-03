package cn.dlj1.gateway.filters;

import cn.dlj1.auth.AuthCheckService;
import cn.dlj1.auth.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    AuthCheckService authCheckService;
    @Autowired
    Config.Props props;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return authCheckService
                .check(exchange)
                .flatMap(integer -> {

                    if (integer == 1) {
                        return chain.filter(exchange);
                    }

                    String location = null;
                    if (integer == -1) {
                        location = props.getLoginHtml();
                        String path = exchange.getRequest().getPath().value();
                        if (!path.startsWith(location)) {
                            try {
                                location = location + "?to=" + URLEncoder.encode(path, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    if (integer == 0) {
                        location = props.getNoAccessHtml();
                    }

                    exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                    exchange.getResponse().getHeaders().set("Location", location);
                    return exchange.getResponse().setComplete();

                });
    }

    @Override
    public int getOrder() {
        return 1;
    }


}