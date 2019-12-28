package cn.dlj1.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@EnableBinding({Sink.class, Source.class})
@EnableScheduling
@SpringBootApplication
public class StreamApplication {

    @Autowired
    Source source;


    public static void main(String[] args) {
        SpringApplication.run(StreamApplication.class);
    }

    @Scheduled(fixedDelay = 5000)
    public void scheduling(){
            source.output().send(
                    MessageBuilder
                            .withPayload(new Person(UUID.randomUUID().toString()))
                            .setHeader("x-expires", 1)
                            .setHeader("x-message-ttl", 1)
                            .build()
            );
    }


    @StreamListener(Sink.INPUT)
    public void handle(Person person) {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Received1: " + person);
    }

    public static class Person {

        public Person(){}

        public Person(String name){
            this.name = name;
        }

        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String toString() {
            return this.name;
        }
    }

}
