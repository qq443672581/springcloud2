package cn.dlj1.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@EnableBinding({Processor.class})
@RestController
@SpringBootApplication
public class StreamApplication {

    @Autowired
    Source source;


    public static void main(String[] args) {
        SpringApplication.run(StreamApplication.class);
    }

    @GetMapping
    public String scheduling(){
        source.output().send(
                MessageBuilder
                        .withPayload(new Person())
                        .build()
        );
        return "ok";
    }

    @StreamListener(Sink.INPUT)
    public void handle(Person person) throws InterruptedException {
        System.out.println("Received1: " + person);
    }

    public static class Person {

        public Person(){
            this(UUID.randomUUID().toString());
        }

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
