package cn.dlj1.gateway;

import cn.dlj1.gateway.filters.AuthFilter;
import cn.dlj1.gateway.filters.LogFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fivewords
 * @date 2019/6/27 15:14
 */
@SpringBootApplication
@RestController
public class GatewayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayApplication.class).run(args);
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public LogFilter logFilter() {
        return new LogFilter();
    }

//    @Bean
//    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(p -> p
//                        .path("/book/hello")
//                        .filters(f ->
//                                f.filters(new LogFilter())
//                                        .addRequestHeader("Hello", "World")
//                                        .addResponseHeader("aaaaaaaa", "vvvvvvvvvvvvvvv")
//                        )
//                        .uri("http://127.0.0.1:8888"))
//                .build();
//    }

}
