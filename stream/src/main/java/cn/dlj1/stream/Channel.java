package cn.dlj1.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Channel {

    @Input(RabbitConfig.LISTENER)
    MessageChannel input();

    @Output(RabbitConfig.NOW_OUTPUT)
    MessageChannel nowOutput();

    @Output(RabbitConfig.DELAY_OUTPUT)
    MessageChannel delayOutput();

}
