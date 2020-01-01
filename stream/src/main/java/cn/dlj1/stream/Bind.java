package cn.dlj1.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.*;
import org.springframework.cloud.stream.binder.rabbit.properties.RabbitExtendedBindingProperties;
import org.springframework.cloud.stream.binding.BindingService;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

import java.util.HashMap;
import java.util.Map;

//@Configuration
public class Bind {

    @Bean
    public BindingService bindingService(
            @Autowired(required = false) BindingServiceProperties bindingServiceProperties,
            @Autowired(required = false) BinderFactory binderFactory,
            TaskScheduler taskScheduler) {

        if (bindingServiceProperties == null) {
            bindingServiceProperties = new BindingServiceProperties();
        }
        if (binderFactory == null) {
            Map<String, BinderConfiguration> binderConfigurations = new HashMap<>();

            Map<String, BinderType> binderTypes = new HashMap<>();
            BinderTypeRegistry binderTypeRegistry = new DefaultBinderTypeRegistry(binderTypes);
            binderFactory = new DefaultBinderFactory(binderConfigurations, binderTypeRegistry);
        }

        Map<String, BindingProperties> bindings = new HashMap<>();

        BindingProperties bindingProperties = new BindingProperties();
        bindingProperties.setDestination("PUSH_CENTER_OUTPUT");
        bindingProperties.setBinder("rabbit");
        RabbitExtendedBindingProperties rabbitExtendedBindingProperties = new RabbitExtendedBindingProperties();
        ExtendedProducerProperties p = new ExtendedProducerProperties(rabbitExtendedBindingProperties);
        bindingProperties.setProducer(p);
        bindings.put("PUSH_CENTER_OUTPUT", bindingProperties);


        bindingProperties = new BindingProperties();
        bindingProperties.setDestination("PUSH_CENTER_INPUT");
        bindingProperties.setGroup("input");
        bindingProperties.setBinder("rabbit");
        rabbitExtendedBindingProperties = new RabbitExtendedBindingProperties();
        p = new ExtendedProducerProperties(rabbitExtendedBindingProperties);
        bindingProperties.setProducer(p);
        bindings.put("PUSH_CENTER_INPUT", bindingProperties);

        bindingProperties = new BindingProperties();
        bindingProperties.setDestination("PUSH_CENTER_INPUT_2");
        bindingProperties.setGroup("input");
        bindingProperties.setBinder("rabbit");
        rabbitExtendedBindingProperties = new RabbitExtendedBindingProperties();
        p = new ExtendedProducerProperties(rabbitExtendedBindingProperties);
        bindingProperties.setProducer(p);
        bindings.put("PUSH_CENTER_INPUT_2", bindingProperties);


        bindingServiceProperties.setBindings(bindings);


        return new BindingService(bindingServiceProperties, binderFactory, taskScheduler);
    }

//    @StreamListener(value = "PUSH_CENTER_INPUT", condition = "headers['flag']=='aa'")
//    public void x(Object o) {
//        System.out.println(o);
//    }


}
