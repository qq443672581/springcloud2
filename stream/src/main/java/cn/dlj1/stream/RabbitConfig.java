package cn.dlj1.stream;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.support.ValueExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.*;
import org.springframework.cloud.stream.binder.rabbit.properties.RabbitConsumerProperties;
import org.springframework.cloud.stream.binder.rabbit.properties.RabbitProducerProperties;
import org.springframework.cloud.stream.binding.BindingService;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    /**
     * 交互机前缀
     */
    public static final String PREFIX = "MESSAGE_PUSH_CENTER";

    public static final String NOW_OUTPUT = PREFIX + "_NOW";
    public static final String DELAY_OUTPUT = PREFIX;
    public static final String LISTENER = PREFIX + "_LISTENER";

    public static final String LISTENER_QUEUE = "LISTENER_QUEUE";
    public static final String PUSH_CENTER_LISTENER_QUEUE = PREFIX + "." + LISTENER_QUEUE;

    public static final String PUSH_CENTER_EXPIRE_QUEUE = PREFIX + ".EXPIRE_QUEUE";
    public static final String PUSH_CENTER_EXPIRE_ROUTING_KEY = PREFIX + "_EXPIRE_ROUTING_KEY";

    @Bean
    public Queue pushCenterExpireQueue() {
        return QueueBuilder
                .durable(PUSH_CENTER_EXPIRE_QUEUE)
                .withArgument("x-message-ttl", 1000 * 5)
                .withArgument("x-dead-letter-exchange", "DLX")
                .withArgument("x-dead-letter-routing-key", PUSH_CENTER_EXPIRE_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue pushCenterListenerQueue() {
        return QueueBuilder
                .durable(PUSH_CENTER_LISTENER_QUEUE)
                .build();
    }

    @Bean
    public Exchange dlx() {
        return ExchangeBuilder.topicExchange("DLX").build();
    }

    @Bean
    public Exchange delayExchange() {
        return ExchangeBuilder.topicExchange(DELAY_OUTPUT).build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(pushCenterListenerQueue())
                .to(dlx())
                .with(PUSH_CENTER_EXPIRE_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Binding bindingListener() {
        return BindingBuilder
                .bind(pushCenterExpireQueue())
                .to(delayExchange())
                .with(DELAY_OUTPUT)
                .noargs();
    }


    @Bean
    public BindingService bindingService(
            @Autowired(required = false) BindingServiceProperties bindingServiceProperties,
            @Autowired(required = false) BinderFactory binderFactory,
            TaskScheduler taskScheduler) {

        if (bindingServiceProperties == null) {
            bindingServiceProperties = new BindingServiceProperties();
        }
        if (binderFactory == null) {
            binderFactory = new DefaultBinderFactory(new HashMap<>(), new DefaultBinderTypeRegistry(new HashMap<>()));
        }
        Map<String, BindingProperties> bindings = new HashMap<>();

        createChannel(bindings, DELAY_OUTPUT, null, null, null);
        createChannel(bindings, NOW_OUTPUT, null, producerProperties(), null);
        createChannel(bindings, LISTENER, LISTENER_QUEUE, null, consumerProperties());

        bindingServiceProperties.setBindings(bindings);
        return new BindingService(bindingServiceProperties, binderFactory, taskScheduler);
    }

    private void createChannel(Map<String, BindingProperties> bindings, String name, String group,
                               ProducerProperties producerProperties, ConsumerProperties consumerProperties) {
        BindingProperties bindingProperties = new BindingProperties();
        bindingProperties.setDestination(PREFIX);
        bindingProperties.setBinder("rabbit");
        bindingProperties.setGroup(group);
        bindingProperties.setProducer(producerProperties);
        bindingProperties.setConsumer(consumerProperties);


        bindings.put(name, bindingProperties);
    }

    private ExtendedProducerProperties producerProperties() {
        RabbitProducerProperties rabbitProducerProperties = new RabbitProducerProperties();
        rabbitProducerProperties.setRoutingKeyExpression(new ValueExpression<>(LISTENER_QUEUE));
        return new ExtendedProducerProperties<>(rabbitProducerProperties);
    }

    private ExtendedConsumerProperties consumerProperties() {
        RabbitConsumerProperties rabbitConsumerProperties = new RabbitConsumerProperties();
        rabbitConsumerProperties.setBindingRoutingKey(LISTENER_QUEUE);
        rabbitConsumerProperties.setAutoBindDlq(true);
        rabbitConsumerProperties.setRepublishToDlq(true);
        return new ExtendedConsumerProperties(rabbitConsumerProperties);
    }


}
