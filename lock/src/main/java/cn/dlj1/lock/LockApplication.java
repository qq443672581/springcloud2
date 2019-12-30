package cn.dlj1.lock;

import com.sun.scenario.effect.LockableResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LockApplication {
    public static void main(String[] args) {
        SpringApplication.run(LockableResource.class);
    }
}
