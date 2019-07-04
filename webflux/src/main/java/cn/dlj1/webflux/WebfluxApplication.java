package cn.dlj1.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class WebfluxApplication {

    public static void main(String[] args) {
       new SpringApplication(WebfluxApplication.class).run(args);
    }

    @GetMapping
    public Mono<String> c(){
        return Mono.empty();
    }

}
