package eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author fivewords
 * @date 2019/5/30 14:01
 */
@SpringBootApplication
public class EurekaStart {

    public static void main(String[] args) {
        SpringApplication.run(EurekaStart.class, args);
    }

}
