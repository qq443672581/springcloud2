package cn.dlj1.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

/**
 * @author fivewords
 * @date 2019/7/11 17:57
 */
@SpringBootApplication
public class MQApplication {

    public static void main(String[] args) {
        SpringApplication.run(MQApplication.class, args);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("114.67.71.39");
        factory.setUsername("admin");
        factory.setPassword("");
        factory.setVirtualHost("test");
        return factory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public Queue A() {
        return new Queue("a");
    }

    @Bean
    public Queue B() {
        return new Queue("b");
    }

    @Bean
    public Queue C() {
        return new Queue("c");
    }

    @Bean
    TopicExchange fanoutExchange() {
        return new TopicExchange("TopicExchange");
    }

    @Bean
    public Binding bindingExchangeWithA() {
        return BindingBuilder.bind(A()).to(fanoutExchange()).with("a.#");
    }

    @Bean
    public Binding bindingExchangeWithB() {
        return BindingBuilder.bind(B()).to(fanoutExchange()).with("b.#");
    }

    @Bean
    public Binding bindingExchangeWithC() {
        return BindingBuilder.bind(C()).to(fanoutExchange()).with("c.#");
    }

    @RabbitListener(queues = {"b"})
    public void fanoutA(Date message) {
        System.out.println("fanout : " + message);
    }

    @Autowired
    AmqpTemplate amqpTemplate;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            Date date = new Date();
            amqpTemplate.convertAndSend("TopicExchange", "a.b.c", date);
        };
    }


}
