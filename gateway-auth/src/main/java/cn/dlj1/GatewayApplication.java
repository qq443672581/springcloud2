package cn.dlj1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.server.session.WebSessionManager;

import java.util.Map;

/**
 *
 */
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