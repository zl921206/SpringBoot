package com.demo.utils;

import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Redis分布式锁工具类
 * @author zhanglei
 * 首先，为了确保分布式锁可用，我们至少要确保锁的实现同时满足以下四个条件：
 *
 * 1：互斥性。在任意时刻，只有一个客户端能持有锁。
 * 2：不会发生死锁。即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁。
 * 3：具有容错性。只要大部分的Redis节点正常运行，客户端就可以加锁和解锁。
 * 4：解铃还须系铃人。加锁和解锁必须是同一个客户端，客户端自己不能把别人加的锁给解了。
 */
@Component
public class RedisLockUtil {

    @Resource
    private RedisTemplate redisTemplate;
    private static RedisTemplate staticRedisTemplate;

    @PostConstruct
    public void initRedisTemplate(){
        staticRedisTemplate = redisTemplate;
    }


    private static final byte[] SCRIPT_RELEASE_LOCK = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end".getBytes();

    /**
     * 尝试获取分布式锁
     *
     * @param key       键
     * @param requestId 请求ID
     * @param expire    锁的有效时间（秒）
     */
    public static synchronized Boolean tryLock(String key, String requestId, long expire) {
        return (Boolean) staticRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.set(key.getBytes(), requestId.getBytes(), Expiration.from(expire, TimeUnit
                .SECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT));
    }

    /**
     * 释放分布式锁
     *
     * @param key       键
     * @param requestId 请求ID
     */
    public static synchronized Boolean releaseLock(String key, String requestId) {
        return (Boolean) staticRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.eval(SCRIPT_RELEASE_LOCK, ReturnType.BOOLEAN, 1, key.getBytes(), requestId.getBytes()));
    }



    /** -------------------------------   使用 Jedis客户端实现分布式锁 ----------------------------- */

    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 尝试获取分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {

        /*
            参数说明：
            第一个为key，我们使用key来当锁，因为key是唯一的。
            第二个为value，我们传的是requestId，原因是我们在上面讲到可靠性时，分布式锁要满足第四个条件解铃还须系铃人，通过给value赋值为requestId，我们就知道这把锁是哪个请求加的了，在解锁的时候就可以有依据。requestId可以使用UUID.randomUUID().toString()方法生成。
            nx()：这个参数我们填的是NX，意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
            px：这个参数我们传的是PX，意思是我们要给这个key加一个过期的设置，
            expireTime：代表key的过期时间。
            set方法执行完的结果：1. 当前没有锁（key不存在），那么就进行加锁操作，并对锁设置个有效期，同时value表示加锁的客户端。2. 已有锁存在，不做任何操作。
         */
        String result = jedis.set(lockKey, requestId, new SetParams().nx().px(expireTime));

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /** -------------------------------   使用 Jedis客户端实现分布式锁 ----------------------------- */



    private static final String DELIMITER = "|";

    /**
     * 获取锁
     *
     * @param lockKey lockKey
     * @param uuid    UUID
     * @param timeout 超时时间
     * @param unit    过期单位
     * @return true or false
     */
    public static boolean lock(String lockKey, final String uuid, long timeout, final TimeUnit unit) {
        final long milliseconds = Expiration.from(timeout, unit).getExpirationTimeInMilliseconds();
//        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, (System.currentTimeMillis() + milliseconds) + DELIMITER + uuid,timeout, TimeUnit.SECONDS);
        Boolean success = staticRedisTemplate.opsForValue().setIfAbsent(lockKey, (System.currentTimeMillis() + milliseconds) + DELIMITER + uuid);

        if (success != null && success) {
            staticRedisTemplate.expire(lockKey, timeout, TimeUnit.SECONDS);
        } else {
            String oldVal = (String) staticRedisTemplate.opsForValue().getAndSet(lockKey, (System.currentTimeMillis() + milliseconds) + DELIMITER + uuid);
            if (oldVal == null) {
                return false;
            }
            final String[] oldValues = oldVal.split(Pattern.quote(DELIMITER));
            if (Long.parseLong(oldValues[0]) + 1 <= System.currentTimeMillis()) {
                return true;
            }

            success = false;
        }
        return success;
    }

    /**
     * @param lockKey key
     * @param uuid    client(最好是唯一键的)
     */
    public static void doUnlock(final String lockKey, final String uuid) {
        String val = (String) staticRedisTemplate.opsForValue().get(lockKey);
        if (val == null) {
            return;
        }
        final String[] values = val.split(Pattern.quote(DELIMITER));
        if (values.length <= 0) {
            return;
        }
        if (uuid.equals(values[1])) {
            staticRedisTemplate.delete(lockKey);
        }
    }

}
