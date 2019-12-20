package com.example.demo.utils.redis.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

/**
 * redis 消息监听器
 */
@Slf4j
@Component
public class RedisMQ implements MessageListener {

    @Value("${spring.redis.mq.topic}")
    private String redisMQTopic;
    @Autowired
    private MessageListener listener;
    @Autowired
    private RedisLockRegistry redisLockRegistry;

    /**
     * reidsTemplate 配置序列化，解决乱码问题
     *
     * 注意： RedisTemplate 如果手动创建，则自动注入的时候要使用 @Resource，而不能使用 @Autowired
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 配置redis 序列化
        RedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer();
        // 默认序列化方式
        redisTemplate.setDefaultSerializer(redisSerializer);
        // 键序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(redisSerializer);
        // hash表序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(redisSerializer);

        return redisTemplate;
    }

    /**
     * 注册 redis监听器
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置redis连接
        container.setConnectionFactory(factory);
        // 添加redis监听器
        container.addMessageListener(listener, new PatternTopic(redisMQTopic));
        // 指定线程(可以防止无法控制队列监听速率，无限制创建线程导致资源占用完)
        container.setTaskExecutor(Executors.newFixedThreadPool(4));
        return container;
    }

    /**
     * redis 分布式锁
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory factory) {
        // redis 分布式锁 相同key，60失效
        return new RedisLockRegistry(factory, redisMQTopic, 60);
    }

    /**
     * 监听redis消息
     *
     * @param message byte[] getBody()    以二进制形式获取休息体
     *                byte[] getChannel() 以二进制形式获取消息通道
     * @param pattern 二进制消息通道，和上述 getChannel() 相同
     */
    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        // redis 分布式锁
        // obtain() 返回的是一个RedisLock锁，私有内部类，实现了Lock接口。所以只能从此方法获取锁，不能通道外部代码来获取锁。
        Lock lock = redisLockRegistry.obtain("lock");

        try {
            lock.lock(); // 上锁

            RedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer();
            log.info("发序列化 消息通道 [ {} ]，消息 [ {} ]", new String(pattern), redisSerializer.deserialize(message.getBody()));
        } catch (Exception e) {
            log.error("redis 队列监听异常 {}", e);
        } finally {
            lock.unlock(); // 解锁
        }
    }

}
