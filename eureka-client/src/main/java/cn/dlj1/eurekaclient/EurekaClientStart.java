package cn.dlj1.eurekaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author fivewords
 * @date 2019/5/30 14:01
 */
@SpringBootApplication
@EnableEurekaClient
public class EurekaClientStart {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientStart.class, args);
    }

}
