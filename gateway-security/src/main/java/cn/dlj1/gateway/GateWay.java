package cn.dlj1.gateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

/**
 * @author fivewords
 * @date 2019/6/27 18:19
 */
@SpringBootApplication
@EnableWebFluxSecurity
public class GateWay {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GateWay.class).run(args);
    }

}