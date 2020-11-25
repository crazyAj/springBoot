package com.example.demo.common.mq;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class RabbitmqManualDemo {

    private static String EXCHANGE_NAME = "";
    private static String QUEUE_NAME = "test_simple_queue";
    public static int ALIVETIME = 30000;

    @Test
    public void producer() throws InterruptedException {
        Connection connection = null;
        Channel channel = null;
        int i = 0;
        QUEUE_NAME = "equipcontrol_auto_unique_queue";
        while (true) {
            try {
                String op = new Scanner(System.in).next();
                if ("q".equals(op)) {
                    if (connection == null || !connection.isOpen()) {
                        //获取一个链接
                        connection = ConnectionUtils.getConnection();
                    }
                    if (channel == null || !channel.isOpen()) {
                        //从连接中获取一个通道
                        channel = connection.createChannel();
                    }
                    // channel.queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
                    // 设置消息存活时间
                    Map<String, Object> argMap = new HashMap<>();
                    argMap.put("x-message-ttl", ALIVETIME);
                    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                    System.out.println("queue finished");
                } else if ("s".equals(op)) {
                    if (connection == null || !connection.isOpen()) {
                        //获取一个链接
                        connection = ConnectionUtils.getConnection();
                    }
                    if (channel == null || !channel.isOpen()) {
                        //从连接中获取一个通道
                        channel = connection.createChannel();
                    }
                    //发送一个消息
//                    String msg = "Hello World ! -- " + i++;
                    Map<String, Object> map = new HashMap();
                    map.put("u", Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                    map.put("e", "FB-A002-AC3BDA2");
                    map.put("k", "4");
                    map.put("v", "4");
                    String msg = JSONObject.toJSONString(map);
                    // channel.basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body) throws IOException;
                    channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, msg.getBytes());
                    System.out.println("msg sended");
                } else if ("d".equals(op)) {
                    //关闭通道和连接
                    if (channel != null && channel.isOpen()) {
                        channel.close();
                    }
                    if (connection != null && connection.isOpen()) {
                        connection.close();
                    }
                    System.out.println("closed");
                }
            } catch (Exception e) {
                log.error("{}", e);
            }
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Test
    public void consumer() throws InterruptedException {
        Connection connection = null;
        Channel channel = null;
        Map<String, Channel> chs = new HashMap<>();
        while (true) {
            try {
                final String op = new Scanner(System.in).next();
                if ("r".equals(op) || "a".equals(op)) {
                    if (connection == null || !connection.isOpen()) {
                        //获取一个链接
                        connection = ConnectionUtils.getConnection();
                    }
                    if (channel == null || !channel.isOpen()) {
                        //从连接中获取一个通道
                        channel = connection.createChannel();
                        chs.put("ch", channel);
                    }
                    final Channel ch = chs.get("ch");
                    Consumer consumer = new DefaultConsumer(ch) {
                        @Override
                        public void handleDelivery(String consumerTag,
                                                   Envelope envelope,
                                                   AMQP.BasicProperties properties,
                                                   byte[] body)
                                throws IOException {
                            System.out.println("msg -- " + new String(body, "UTF-8"));
                            if ("a".equals(op)) {
                                ch.basicAck(envelope.getDeliveryTag(), false);
                                System.out.println("msg received");
                            } else {
                                System.out.println("no ack");
                            }
                        }
                    };
                    // basicConsume(String queue, boolean autoAck, DeliverCallback deliverCallback, CancelCallback cancelCallback)
                    channel.basicConsume("testDtu1", false, consumer);
                } else if ("d".equals(op)) {
                    //关闭通道和连接
                    if (channel != null && channel.isOpen()) {
                        channel.close();
                    }
                    if (connection != null && connection.isOpen()) {
                        connection.close();
                    }
                    System.out.println("closed");
                }
            } catch (Exception e) {
                log.error("{}", e);
            }
            TimeUnit.SECONDS.sleep(1);
        }
    }

}

class ConnectionUtils {

    /**
     * 获取MQ的连接
     */
    public static Connection getConnection() throws IOException, TimeoutException {
        //定义一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
//        //设置服务器地址
//        factory.setHost("192.168.11.164");
//        // AMQP 端口
//        factory.setPort(5672);
//        //vhost
//        factory.setVirtualHost("/");
//        //用户名
//        factory.setUsername("aj");
//        //密码
//        factory.setPassword("qwer");

        //设置服务器地址
        factory.setHost("192.168.15.172");
        // AMQP 端口
        factory.setPort(5672);
        //vhost
        factory.setVirtualHost("/");
        //用户名
        factory.setUsername("root");
        //密码
        factory.setPassword("root");
        return factory.newConnection();
    }

}