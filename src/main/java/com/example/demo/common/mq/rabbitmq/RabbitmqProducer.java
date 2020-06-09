package com.example.demo.common.mq.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
//@Component
public class RabbitmqProducer {

    @Resource(name = "firstRabbitTemplate")
    private RabbitTemplate firstRabbitTemplate;

    @Resource(name = "secondRabbitTemplate")
    private RabbitTemplate secondRabbitTemplate;

    public void sendRabbitmqMessageToFirst(String exchage, String routingKey, String msg) {
        log.info("--- RabbitmqProducer --- sendRabbitmqMessageToFirst --- exchage = {} --- routingKey = {} --- msg = {}", exchage, routingKey, msg);
        firstRabbitTemplate.convertAndSend(exchage, routingKey, msg);
    }

    public void sendRabbitmqMessageToSecond(String exchage, String routingKey, String msg) {
        log.info("--- RabbitmqProducer --- sendRabbitmqMessageToSecond --- exchage = {} --- routingKey = {} --- msg = {}", exchage, routingKey, msg);
        secondRabbitTemplate.convertAndSend(exchage, routingKey, msg);
    }

}
