package cn.dlj1.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class WebfluxApplication {

    public static void main(String[] args) {
        new SpringApplication(WebfluxApplication.class).run(args);
    }

    @GetMapping("/")
    public Mono<String> c() {
        return Mono.just("你好!").delayElement(Duration.ofMillis(100));
    }

    @GetMapping("/mono")
    public Flux<List> mono() {
        List<String> a = new ArrayList<>();
        a.add("你好");
        a.add("flux");
        return Flux.just(a);
    }

}
