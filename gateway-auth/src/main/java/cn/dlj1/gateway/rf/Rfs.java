package cn.dlj1.gateway.rf;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

/**
 * 断言
 */
@Configurable
@RestController
public class Rfs {

    @GetMapping("/debug")
    public Mono<String> debug(WebSession webSession) {
        return Mono.just("ok");
    }

    /**
     * debug断言
     *
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> testFunRouterFunction() {
        RouterFunction<ServerResponse> route = RouterFunctions.route(
                RequestPredicates.queryParam("debug", "true"),
                request -> ServerResponse.ok().body(BodyInserters.fromObject("debug..."))
        );
        return route;
    }

}
