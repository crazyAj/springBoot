package com.example.demo.common.mq.rabbitmq;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 实现RabbitAdmin后，根据RabbitAdmin创建对应的交换机和队列，并建立绑定关系
 */
@Configuration
public class RabbitMQBindingConfig {

    @Resource(name = "firstRabbitAdmin")
    private RabbitAdmin firstRabbitAdmin;
    @Resource(name = "secondRabbitAdmin")
    private RabbitAdmin secondRabbitAdmin;

    @Value("${rabbitmq.exchange.first.exchange}")
    private String firstExchange;
    @Value("${rabbitmq.queue.first.routing-key}")
    private String firstRoutingKey;
    @Value("${rabbitmq.queue.first.key-name}")
    private String firstQueue;

    @Value("${rabbitmq.exchange.second.exchange}")
    private String secondExchange;
    @Value("${rabbitmq.queue.second.routing-key}")
    private String secondRoutingKey;
    @Value("${rabbitmq.queue.second.key-name}")
    private String secondQueue;

    @PostConstruct
    public void init() {
        firstRabbitAdmin.declareExchange(new TopicExchange(firstExchange));
        firstRabbitAdmin.declareQueue(new Queue(firstQueue));
        firstRabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue(firstQueue))
                        .to(new TopicExchange(firstExchange))
                        .with(firstRoutingKey));

        secondRabbitAdmin.declareExchange(new TopicExchange(secondExchange));
        secondRabbitAdmin.declareQueue(new Queue(secondQueue));
        secondRabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue(secondQueue))
                        .to(new TopicExchange(secondExchange))
                        .with(secondRoutingKey));
    }

}
