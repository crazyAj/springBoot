package com.example.demo.extra.others;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MQTT_DEMO {

    private static final String BROKER = "tcp://192.168.11.164:1883";
    private static final String USERNAME = "emqx_test";
    private static final String PASSWORD = "emqx_test_password";
    private static final String SEND_CLIENTID = "emqx_send";
    private static final String PUB_TOPIC = "testtopic/1";
    private static final String REC_CLIENTID = "emqx_receive";
    private static final String SUB_TOPIC = "testtopic/1";
    private static final int QOS = 2;

    private static MqttClient server;
    private static MqttClient client;

    @Test
    public void send() throws Exception {
        int i = 0;
        server = new MqttClient(BROKER, SEND_CLIENTID, new MemoryPersistence());
        // MQTT 连接选项
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(USERNAME);
        connOpts.setPassword(PASSWORD.toCharArray());
        // 保留会话
        connOpts.setCleanSession(true);
        // 建立连接
        System.out.println("SEND - Connecting to broker: " + BROKER);
        server.connect(connOpts);
        System.out.println("SEND - Connected");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String content = "Hello World -- " + i++;
            try {
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    if ("s".equals(scanner.next())) break;
                }
                // 消息发布所需参数
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(QOS);
                // 断开连接保存消息
                message.setRetained(true);
                server.publish(PUB_TOPIC, message);
                System.out.println("SEND - Message published, message: " + content);
            } catch (Exception e) {
                log.error("{}", e);
                server.disconnect();
                System.out.println("SEND - Disconnected");
                server.close();
            }
        }
    }

    @Test
    public void receive() throws Exception {
        client = new MqttClient(BROKER, REC_CLIENTID, new MemoryPersistence());
        // MQTT 连接选项
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(USERNAME);
        connOpts.setPassword(PASSWORD.toCharArray());
        // 保留会话
        connOpts.setCleanSession(true);
        // 自动重连接
        connOpts.setAutomaticReconnect(true);
        // 设置回调
        client.setCallback(new OnMessageCallback());
//        client.setManualAcks(true);
        // 建立连接
        System.out.println("REC - Connecting to broker: " + BROKER);
        client.connect(connOpts);
        System.out.println("REC - Connected");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
                String fun = scanner.next();

                if ("r".equals(fun)) {
                    // 订阅
                    client.subscribe(SUB_TOPIC, QOS);
                    System.out.println("订阅");
                } else if ("u".equals(fun)) {
                    // 退订
                    client.unsubscribe(SUB_TOPIC);
                    System.out.println("退订");
                } else if ("d".equals(fun)) {
                    client.disconnect();
                    System.out.println("断开");
                } else if ("re".equals(fun)) {
                    client.reconnect();
                    System.out.println("重连");
                }
            } catch (Exception e) {
                log.error("{}", e);
                client.disconnect();
                System.out.println("REC - Disconnected");
            }
        }
    }

    /**
     * 回调函数
     */
    class OnMessageCallback implements MqttCallbackExtended {

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            // subscribe后得到的消息会执行到这里面
            System.out.println(String.format("REC - 接收消息主题：%s， 接收消息Qos：%s, 接收消息内容：%s",
                    topic,
                    message.getQos(),
                    new String(message.getPayload())));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            System.out.println("REC - deliveryComplete -- " + token.isComplete());
        }

        /**
         * 连接丢失后，一般在这里面进行重连
         */
        @Override
        public void connectionLost(Throwable cause) {
            while (true) {
                try {
                    System.out.println("REC - 连接断开，重连");
                    MQTT_DEMO.client.reconnect();
                    break;
                } catch (MqttException e) {
                    log.error("{}", e);
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }

        /**
         * 连接重连后，重新订阅
         */
        @Override
        public void connectComplete(boolean b, String s) {
            while (true) {
                try {
                    System.out.println("REC - 连接断开，重新订阅");
                    MQTT_DEMO.client.subscribe(SUB_TOPIC, QOS);
                    break;
                } catch (MqttException e) {
                    log.error("{}", e);
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }

    }

}