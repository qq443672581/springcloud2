package cn.dlj1.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@EnableBinding({Channel.class})
@RestController
@SpringBootApplication
public class StreamApplication {

    @Autowired
    Channel channel;


    public static void main(String[] args) {
        SpringApplication.run(StreamApplication.class);
    }

    @GetMapping
    public String get() {
        channel.delayOutput().send(
                MessageBuilder
                        .withPayload(new Date())
                        .setHeader("a", "1")
                        .build()
        );
        return "ok";
    }

    @StreamListener(value = RabbitConfig.LISTENER, condition = "headers['a'] == '1'")
    public void handle(Object obj) {
        throw new RuntimeException("6666666666");
    }

}
