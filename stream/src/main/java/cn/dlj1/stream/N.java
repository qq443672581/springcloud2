package cn.dlj1.stream;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.support.ValueExpression;
import org.springframework.amqp.support.postprocessor.DelegatingDecompressingPostProcessor;
import org.springframework.amqp.support.postprocessor.GZipPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.binder.rabbit.RabbitMessageChannelBinder;
import org.springframework.cloud.stream.binder.rabbit.properties.*;
import org.springframework.cloud.stream.binder.rabbit.provisioning.RabbitExchangeQueueProvisioner;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@Import({PropertyPlaceholderAutoConfiguration.class})
@EnableConfigurationProperties({RabbitBinderConfigurationProperties.class, RabbitExtendedBindingProperties.class})
public class N {

    @Autowired
    private ConnectionFactory rabbitConnectionFactory;

    @Autowired
    private RabbitProperties rabbitProperties;

    @Autowired
    private RabbitBinderConfigurationProperties rabbitBinderConfigurationProperties;

    @Autowired
    private RabbitExtendedBindingProperties rabbitExtendedBindingProperties;

    @Bean
    RabbitMessageChannelBinder rabbitMessageChannelBinder(@Nullable ListenerContainerCustomizer<AbstractMessageListenerContainer> listenerContainerCustomizer) throws Exception {
        RabbitMessageChannelBinder binder = new RabbitMessageChannelBinder(this.rabbitConnectionFactory,
                this.rabbitProperties, provisioningProvider(), listenerContainerCustomizer);
        binder.setAdminAddresses(this.rabbitBinderConfigurationProperties.getAdminAddresses());
        binder.setCompressingPostProcessor(gZipPostProcessor());
        binder.setDecompressingPostProcessor(deCompressingPostProcessor());
        binder.setNodes(this.rabbitBinderConfigurationProperties.getNodes());
        binder.setExtendedBindingProperties(getRabbitExtendedBindingProperties());
        return binder;
    }

    private RabbitExtendedBindingProperties getRabbitExtendedBindingProperties() {
        Map<String, RabbitBindingProperties> bindings = new HashMap<>();

        RabbitBindingProperties properties = new RabbitBindingProperties();
        properties.setConsumer(consumerProperties());
        properties.setProducer(new RabbitProducerProperties());
        bindings.put(RabbitConfig.LISTENER, properties);

        properties = new RabbitBindingProperties();
        properties.setConsumer(new RabbitConsumerProperties());
        properties.setProducer(producerProperties());
        bindings.put(RabbitConfig.NOW_OUTPUT, properties);

        this.rabbitExtendedBindingProperties.setBindings(bindings);
        return this.rabbitExtendedBindingProperties;
    }


    private RabbitConsumerProperties consumerProperties() {
        RabbitConsumerProperties rabbitConsumerProperties = new RabbitConsumerProperties();
        rabbitConsumerProperties.setBindingRoutingKey(RabbitConfig.LISTENER_QUEUE);
        rabbitConsumerProperties.setAutoBindDlq(true);
        rabbitConsumerProperties.setRepublishToDlq(true);
        return rabbitConsumerProperties;
    }

    private RabbitProducerProperties producerProperties() {
        RabbitProducerProperties rabbitProducerProperties = new RabbitProducerProperties();
        rabbitProducerProperties.setRoutingKeyExpression(new ValueExpression<>(RabbitConfig.LISTENER_QUEUE));
        return rabbitProducerProperties;
    }

    @Bean
    MessagePostProcessor deCompressingPostProcessor() {
        return new DelegatingDecompressingPostProcessor();
    }

    @Bean
    MessagePostProcessor gZipPostProcessor() {
        GZipPostProcessor gZipPostProcessor = new GZipPostProcessor();
        gZipPostProcessor.setLevel(this.rabbitBinderConfigurationProperties.getCompressionLevel());
        return gZipPostProcessor;
    }

    @Bean
    RabbitExchangeQueueProvisioner provisioningProvider() {
        return new RabbitExchangeQueueProvisioner(this.rabbitConnectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean(ConnectionNameStrategy.class)
    @ConditionalOnProperty("spring.cloud.stream.rabbit.binder.connection-name-prefix")
    public ConnectionNameStrategy connectionNamer(CachingConnectionFactory cf) {
        final AtomicInteger nameIncrementer = new AtomicInteger();
        ConnectionNameStrategy namer = f -> this.rabbitBinderConfigurationProperties.getConnectionNamePrefix()
                + "#" + nameIncrementer.getAndIncrement();
        // TODO: this can be removed when Boot 2.0.1 wires it in
        cf.setConnectionNameStrategy(namer);
        return namer;
    }

}
