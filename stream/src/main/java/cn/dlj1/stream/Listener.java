package cn.dlj1.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.binder.rabbit.RabbitMessageChannelBinder;
import org.springframework.cloud.stream.binder.rabbit.config.RabbitMessageChannelBinderConfiguration;
import org.springframework.cloud.stream.binder.rabbit.properties.RabbitExtendedBindingProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

@Component
public class Listener implements CommandLineRunner {

    @Autowired
    private WebApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        Map<String, RabbitMessageChannelBinder> beansOfType = applicationContext.getBeansOfType(RabbitMessageChannelBinder.class);
        Map<String, RabbitMessageChannelBinderConfiguration> beansOfType2 = applicationContext.getBeansOfType(RabbitMessageChannelBinderConfiguration.class);

        System.out.println();

    }

}
