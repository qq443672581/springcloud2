package cn.dlj1.file_sender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FileSenderApplication {

    public static void main(String[] args) {
        new SpringApplication(FileSenderApplication.class).run(args);
    }
}
