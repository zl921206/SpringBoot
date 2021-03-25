package com.demo.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * redis key失效监听器
 * 注：开启此监听器，需要修改redis.conf 配置文件中：notify-keyspace-events ""， 改为：notify-keyspace-events "Ex"
 */
@Component
public class RedisKeyExpireListener extends KeyExpirationEventMessageListener {

    public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer){
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 获取到redis失效key，进行业务逻辑处理
        String expireKey = message.toString();
        System.out.println("expireKey: " + expireKey);
    }
}
