package com.weixf.utils.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * 本地缓存提供者服务 (Guava Cache)
 * <p>
 * 注意事项
 * Guava Cache初始化容器时，支持缓存过期策略，类似FIFO、LRU和LFU等算法。
 * expireAfterWrite：最后一次写入后的一段时间移出。
 * expireAfterAccess：最后一次访问后的一段时间移出。
 *
 * @since 2023-02-06
 */
public class LocalCacheProviderImpl implements CacheProviderService {

    private static final Map<String, Cache<String, Object>> _cacheMap = Maps.newConcurrentMap();
    private static final Lock lock = new ReentrantLock();

    static {
        Cache<String, Object> cacheContainer = CacheBuilder.newBuilder()
                .maximumSize(AppConst.CACHE_MAXIMUM_SIZE)
                .expireAfterWrite(AppConst.CACHE_MINUTE, TimeUnit.MILLISECONDS)// 最后一次写入后的一段时间移出
                //.expireAfterAccess(AppConst.CACHE_MINUTE, TimeUnit.MILLISECONDS) //最后一次访问后的一段时间移出
                .recordStats()// 开启统计功能
                .build();
        _cacheMap.put(String.valueOf(AppConst.CACHE_MINUTE), cacheContainer);
    }

    /**
     * 查询缓存
     *
     * @param key 缓存键 不可为空
     **/
    @Override
    public <T> T get(String key) {
        return get(key, null, null, AppConst.CACHE_MINUTE);
    }

    /**
     * 查询缓存
     *
     * @param key      缓存键 不可为空
     * @param function 如没有缓存，调用该callable函数返回对象 可为空
     **/
    @Override
    public <T> T get(String key, Function<String, T> function) {
        return get(key, function, key, AppConst.CACHE_MINUTE);
    }

    /**
     * 查询缓存
     *
     * @param key      缓存键 不可为空
     * @param function 如没有缓存，调用该callable函数返回对象 可为空
     * @param funcParm function函数的调用参数
     **/
    @Override
    public <T, M> T get(String key, Function<M, T> function, M funcParm) {
        return get(key, function, funcParm, AppConst.CACHE_MINUTE);
    }

    /**
     * 查询缓存
     *
     * @param key        缓存键 不可为空
     * @param function   如没有缓存，调用该callable函数返回对象 可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     **/
    @Override
    public <T> T get(String key, Function<String, T> function, Long expireTime) {
        return get(key, function, key, expireTime);
    }

    /**
     * 查询缓存
     *
     * @param key        缓存键 不可为空
     * @param function   如没有缓存，调用该callable函数返回对象 可为空
     * @param funcParam  function函数的调用参数
     * @param expireTime 过期时间（单位：毫秒） 可为空
     **/
    @Override
    public <T, M> T get(String key, Function<M, T> function, M funcParam, Long expireTime) {
        T obj = null;
        if (ObjectUtils.isEmpty(key)) {
            return null;
        }
        expireTime = getExpireTime(expireTime);
        Cache<String, Object> cacheContainer = getCacheContainer(expireTime);
        try {
            if (function == null) {
                obj = (T) cacheContainer.getIfPresent(key);
            } else {
                // final Long cachedTime = expireTime;
                obj = (T) cacheContainer.get(key, () -> {
                    T retObj = function.apply(funcParam);
                    return retObj;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * 设置缓存键值 直接向缓存中插入值，这会直接覆盖掉给定键之前映射的值
     *
     * @param key 缓存键 不可为空
     * @param obj 缓存值 不可为空
     **/
    @Override
    public <T> void set(String key, T obj) {
        set(key, obj, AppConst.CACHE_MINUTE);
    }

    /**
     * 设置缓存键值 直接向缓存中插入值，这会直接覆盖掉给定键之前映射的值
     *
     * @param key        缓存键 不可为空
     * @param obj        缓存值 不可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     **/
    @Override
    public <T> void set(String key, T obj, Long expireTime) {
        if (ObjectUtils.isEmpty(key)) {
            return;
        }
        if (obj == null) {
            return;
        }
        expireTime = getExpireTime(expireTime);
        Cache<String, Object> cacheContainer = getCacheContainer(expireTime);
        cacheContainer.put(key, obj);
    }

    /**
     * 移除缓存
     *
     * @param key 缓存键 不可为空
     **/
    @Override
    public void remove(String key) {
        if (ObjectUtils.isEmpty(key)) {
            return;
        }
        long expireTime = getExpireTime(AppConst.CACHE_MINUTE);
        Cache<String, Object> cacheContainer = getCacheContainer(expireTime);
        cacheContainer.invalidate(key);
    }

    /**
     * 是否存在缓存
     *
     * @param key 缓存键 不可为空
     **/
    @Override
    public boolean contains(String key) {
        boolean exists = false;
        if (ObjectUtils.isEmpty(key)) {
            return false;
        }
        Object obj = get(key);
        if (obj != null) {
            exists = true;
        }
        return exists;
    }

    private Cache<String, Object> getCacheContainer(Long expireTime) {
        Cache<String, Object> cacheContainer = null;
        if (expireTime == null) {
            return cacheContainer;
        }
        String mapKey = String.valueOf(expireTime);
        if (_cacheMap.containsKey(mapKey)) {
            cacheContainer = _cacheMap.get(mapKey);
            return cacheContainer;
        }
        try {
            lock.lock();
            cacheContainer = CacheBuilder.newBuilder()
                    .maximumSize(AppConst.CACHE_MAXIMUM_SIZE)
                    .expireAfterWrite(expireTime, TimeUnit.MILLISECONDS)// 最后一次写入后的一段时间移出
                    //.expireAfterAccess(AppConst.CACHE_MINUTE, TimeUnit.MILLISECONDS) //最后一次访问后的一段时间移出
                    .recordStats()// 开启统计功能
                    .build();
            _cacheMap.put(mapKey, cacheContainer);
        } finally {
            lock.unlock();
        }
        return cacheContainer;
    }

    /**
     * 获取过期时间 单位：毫秒
     *
     * @param expireTime 传人的过期时间 单位毫秒 如小于1分钟，默认为10分钟
     **/
    private Long getExpireTime(Long expireTime) {
        Long result = expireTime;
        if (expireTime == null || expireTime < AppConst.CACHE_MINUTE / 10) {
            result = AppConst.CACHE_MINUTE;
        }
        return result;
    }
}
