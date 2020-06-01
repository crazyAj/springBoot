package com.example.demo.common.mq.rabbitmq;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * RabbitMQ 多源配置
 */
@Configuration
public class MultipleRabbitMQConfig {

    /*************************************** init first rabbitmq ****************************************/

    /**
     * init first ConnectionFactory
     * 创建 rabbitmq 连接工厂
     */
    @Primary
    @Bean(name = "firstConnectionFactory")
    public ConnectionFactory firstConnectionFactory(
            @Value("${spring.rabbitmq.first.virtual-host}") String virtual_host,
            @Value("${spring.rabbitmq.first.host}") String host,
            @Value("${spring.rabbitmq.first.port}") int port,
            @Value("${spring.rabbitmq.first.username}") String username,
            @Value("${spring.rabbitmq.first.password}") String password) {
        return connectionFactory(virtual_host, host, port, username, password);
    }

    /**
     * init first RabbitTemplate
     * rabbit mq 模板
     */
    @Primary
    @Bean(name = "firstRabbitTemplate")
    public RabbitTemplate firstRabbitTemplate(@Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    /**
     * init firt SimpleRabbitListenerContainerFactory
     * 消息监听容器
     */
    @Primary
    @Bean(name = "firstContainerFactory")
    public SimpleRabbitListenerContainerFactory firstFactory(
            @Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    /**
     * init first RabbitAdmin
     * 该类封装了对 RabbitMQ 的管理操作
     */
    @Bean(name = "firstRabbitAdmin")
    public RabbitAdmin firstRabbitAdmin(@Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        return rabbitAdmin;
    }


    /*************************************** init second rabbitmq ****************************************/

    /**
     * init second ConnectionFactory
     */
    @Bean(name = "secondConnectionFactory")
    public ConnectionFactory secondConnectionFactory(
            @Value("${spring.rabbitmq.second.virtual-host}") String virtual_host,
            @Value("${spring.rabbitmq.second.host}") String host,
            @Value("${spring.rabbitmq.second.port}") int port,
            @Value("${spring.rabbitmq.second.username}") String username,
            @Value("${spring.rabbitmq.second.password}") String password) {
        return connectionFactory(virtual_host, host, port, username, password);
    }

    /**
     * init second RabbitTemplate
     */
    @Bean(name = "secondRabbitTemplate")
    public RabbitTemplate secondRabbitTemplate(@Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    /**
     * init second SimpleRabbitListenerContainerFactory
     */
    @Bean(name = "secondContainerFactory")
    public SimpleRabbitListenerContainerFactory secondFactory(
            @Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    /**
     * init second RabbitAdmin
     * 该类封装了对 RabbitMQ 的管理操作
     */
    @Bean(name = "secondRabbitAdmin")
    public RabbitAdmin secondRabbitAdmin(@Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        return rabbitAdmin;
    }


    private CachingConnectionFactory connectionFactory(String virtual_host, String host, int port, String username, String password) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtual_host);
        return connectionFactory;
    }

}
