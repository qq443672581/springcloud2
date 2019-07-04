package cn.dlj1.gateway;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

/**
 * @author fivewords
 * @date 2019/7/3 18:32
 */
@Configuration
public class GatewayConfig {

//    @Bean
    public Scheduler jdbcScheduler(HikariDataSource dataSource) {
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(dataSource.getMaximumPoolSize()));
    }

}
