package com.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import com.demo.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;

/**
 * redis 服务接口
 */
@Service
public class RedisService {

    /**
     * 注入redis模板bean
     */
    @Resource
    private RedisTemplate redisTemplate;

    //=============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param directory 存储目录
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String directory, String key, long time) {
        try {
            if (time > 0) {
                key = keyConvert(directory, key);
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param directory 存储目录
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String directory, String key) {
        key = keyConvert(directory, key);
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param directory 存储目录
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String directory, String key) {
        try {
            key = keyConvert(directory, key);
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param directory 存储目录
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String directory, String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                key[0] = keyConvert(directory, key[0]);
                redisTemplate.delete(key[0]);
            } else {
                for(int i = 0; i < key.length; i++){
                    key[i] = keyConvert(directory, key[i]);
                }
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    //============================String=============================

    /**
     * 普通缓存获取
     * @param directory 存储目录
     * @param key
     * @param clazz
     * @param <T>
     * @return T
     */
    public <T> T getObject(String directory, String key, Class<T> clazz) {
        key = keyConvert(directory, key);
        return key == null ? null : (T)redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param directory 存储目录
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setObject(String directory, String key, Object value) {
        try {
            key = keyConvert(directory, key);
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param directory 存储目录
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean setObjectAndTime(String directory, String key, Object value, long time) {
        try {
            if (time > 0) {
                key = keyConvert(directory, key);
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                setObject(directory, key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //================================Hash=================================

    /**
     * 获取Hash值
     * @param directory
     * @param key
     * @param item
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getHashValue(String directory, String key, String item, Class<T> clazz) {
        key = keyConvert(directory, key);
        return (T)redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param directory
     * @param key
     * @return
     */
    public Map<Object, Object> getHashByKey(String directory, String key) {
        key = keyConvert(directory, key);
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 设置Hash值
     * @param directory
     * @param key
     * @param map
     * @return
     */
    public boolean putHashValue(String directory, String key, Map<Object, Object> map) {
        try {
            key = keyConvert(directory, key);
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置Hash值并设置时间
     * @param directory
     * @param key
     * @param map
     * @param time
     * @return
     */
    public boolean putHashValueAndTime(String directory, String key, Map<Object, Object> map, long time) {
        try {
            String originalKey = key;
            key = keyConvert(directory, key);
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(directory, originalKey, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据
     * @param directory
     * @param key
     * @param item
     * @param value
     * @return
     */
    public boolean putHashValue(String directory, String key, String item, Object value) {
        try {
            key = keyConvert(directory, key);
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据并设置时间,如果不存在将创建
     * @param directory
     * @param key
     * @param item
     * @param value
     * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return
     */
    public boolean putHashValueAndTime(String directory, String key, String item, Object value, long time) {
        try {
            String originalKey = key;
            key = keyConvert(directory, key);
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(directory, originalKey, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param directory
     * @param key
     * @param item
     */
    public void delHashValue(String directory, String key, Object... item) {
        key = keyConvert(directory, key);
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param directory
     * @param key
     * @param item
     * @return
     */
    public boolean isContainHashValue(String directory, String key, String item) {
        key = keyConvert(directory, key);
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     * @param directory
     * @param key
     * @return
     */
    public Set<Object> sGetValueByKey(String directory, String key) {
        try {
            key = keyConvert(directory, key);
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据key获取Set中的所有值
     * @param directory
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Set<T> sGetValueByKey(String directory, String key, Class<T> clazz) {
        try {
            key = keyConvert(directory, key);
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param directory
     * @param key
     * @param value
     * @return
     */
    public boolean sIsContainValue(String directory, String key, Object value) {
        try {
            key = keyConvert(directory, key);
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param directory
     * @param key
     * @param values
     * @return
     */
    public long sSetValue(String directory, String key, Object... values) {
        try {
            key = keyConvert(directory, key);
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param directory
     * @param key
     * @param time
     * @param values
     * @return
     */
    public long sSetValueAndTime(String directory, String key, long time, Object... values) {
        try {
            String originalKey = key;
            key = keyConvert(directory, key);
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(directory, originalKey, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param directory
     * @param key
     * @return
     */
    public long sGetSetSize(String directory, String key) {
        try {
            key = keyConvert(directory, key);
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 删除set中数据
     * @param directory
     * @param key
     * @param values
     * @return
     */
    public long sDelSet(String directory, String key, Object... values) {
        try {
            key = keyConvert(directory, key);
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //===============================list=================================

    /**
     * 根据区间获取list缓存的内容
     * @param directory
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> getListByInterval(String directory, String key, long start, long end) {
        try {
            key = keyConvert(directory, key);
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的内容
     * @param directory
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getListAll(String directory, String key, Class<T> clazz) {
        try {
            key = keyConvert(directory, key);
            return redisTemplate.opsForList().range(key, 0, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的内容
     * @param directory
     * @param key
     * @return
     */
    public List<Object> getListAll(String directory, String key) {
        try {
            key = keyConvert(directory, key);
            return redisTemplate.opsForList().range(key, 0, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param directory
     * @param key
     * @return
     */
    public long getListSize(String directory, String key) {
        try {
            key = keyConvert(directory, key);
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param directory
     * @param key
     * @param index
     * @return
     */
    public Object getListByIndex(String directory, String key, long index) {
        try {
            key = keyConvert(directory, key);
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param directory
     * @param key
     * @param index
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getListByIndex(String directory, String key, long index, Class<T> clazz) {
        try {
            key = keyConvert(directory, key);
            return (T)redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param directory
     * @param key
     * @param value
     * @return
     */
    public boolean addList(String directory, String key, List<Object> value) {
        try {
            key = keyConvert(directory, key);
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存并设置时间
     * @param directory
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean addListAndTime(String directory, String key, List<Object> value, long time) {
        try {
            String originalKey = key;
            key = keyConvert(directory, key);
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(directory, originalKey, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将传入目录与key值拼接
     * @param directory
     * @param redisKey
     */
    public String keyConvert(String directory, String redisKey){
        if(StringUtils.isNotEmpty(directory) && StringUtils.isNotEmpty(redisKey)){
            return directory + Constant.symbol + redisKey;
        }
        return redisKey;
    }

}
