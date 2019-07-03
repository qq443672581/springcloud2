package cn.dlj1;

import cn.dlj1.gateway.auth.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.ReactiveRedisOperations;

import javax.sql.DataSource;
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
        Map<String, UserRepository> beansOfType = context.getBeansOfType(UserRepository.class);
        beansOfType.forEach((s, reactiveRedisOperations) -> {
            long count = reactiveRedisOperations.count();
            System.out.println(reactiveRedisOperations.getClass().getName());

        });
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayApplication.class).run(args);
    }

}