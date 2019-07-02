package cn.dlj1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Map;

/**
 *
 */
//@SpringBootApplication(exclude = {RedisReactiveAutoConfiguration.class })
@SpringBootApplication
public class GatewayApplication implements CommandLineRunner {

    @Autowired
    ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        Map<String, RedisConnectionFactory> beansOfType = context.getBeansOfType(RedisConnectionFactory.class);
        System.out.println(beansOfType);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayApplication.class).run(args);
    }

}