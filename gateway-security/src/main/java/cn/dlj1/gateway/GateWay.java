package cn.dlj1.gateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author fivewords
 * @date 2019/6/27 18:19
 */
@SpringBootApplication
public class GateWay {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GateWay.class).run(args);
    }

}

@Component
class UrlKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

}
