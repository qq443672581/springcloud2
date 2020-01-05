package cn.dlj1.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@EnableBinding({Processor.class})
@RestController
@SpringBootApplication
@Slf4j
public class StreamApplication {

    @Autowired
    private Processor processor;

    public static void main(String[] args) {
        SpringApplication.run(StreamApplication.class);
    }

    @GetMapping
    public String get() {

        for (int i = 0; i < 500; i++) {

            processor.output().send(
                    MessageBuilder
                            .withPayload(i)
                            .build()
            );
        }

        return "ok";
    }

    @StreamListener(value = Processor.INPUT)
    public void handle(Object obj) {
        System.out.println(obj);
    }

}
