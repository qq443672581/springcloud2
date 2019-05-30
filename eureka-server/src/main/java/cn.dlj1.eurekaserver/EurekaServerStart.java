package cn.dlj1.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author fivewords
 * @date 2019/5/30 14:01
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerStart {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerStart.class, args);
    }

}
