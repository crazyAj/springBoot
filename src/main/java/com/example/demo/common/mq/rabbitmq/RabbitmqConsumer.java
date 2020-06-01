package com.example.demo.common.mq.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * RabbitListener绑定队列两种方式：(配合 @RabbitHandler 使用)
 *
 * @RabbitListener(queues = {"${rabbitmq.queue.uniteQueue}"})
 * @RabbitListener(containerFactory = "rabbitListenerContainerFactory", bindings = @QueueBinding(
 * value = @Queue(value = "${mq.config.queue}", durable = "true"),
 * exchange = @Exchange(value = "${mq.config.exchange}", type = ExchangeTypes.TOPIC),
 * key = "${mq.config.key}"), admin = "rabbitAdmin")
 */
@Slf4j
@Component
public class RabbitmqConsumer {

    @RabbitHandler
    @RabbitListener(queues = "${rabbitmq.queue.first.key-name}", containerFactory = "firstContainerFactory")
    public void getFirstRabbitmqMessage(Message msg) {
        log.info("--- RabbitmqConsumer --- getRabbitmqMessage -------- msg = " + new String(msg.getBody()));
    }

    @RabbitHandler
    @RabbitListener(queues = "${rabbitmq.queue.second.key-name}", containerFactory = "secondContainerFactory")
    public void getSecondRabbitmqMessage(Message msg) {
        log.info("--- RabbitmqConsumer --- getRabbitmqMessage -------- msg = " + new String(msg.getBody()));
    }

    /**
     * 手动ask：
     */
//    @RabbitHandler
//    @RabbitListener(queues = {"${rabbitmq.queue.uniteQueue}", "${rabbitmq.queue.second.key-name}"})
    public void getRabbitmqMessage2(Message msg, Channel channel) {
        log.info("--- RabbitmqConsumer --- getRabbitmqMessage -------- msg = " + msg.getBody());
        try {
            // 设置没有收到，会重发消息
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
