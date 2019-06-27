package cn.dlj1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import javax.annotation.PostConstruct;

/**
 * @author fivewords
 * @date 2019/6/26 14:01
 */
@SpringBootApplication
public class SecurityApplication {


    public static void main(String[] args) {
        new SpringApplicationBuilder(SecurityApplication.class).run(args);

    }

}
