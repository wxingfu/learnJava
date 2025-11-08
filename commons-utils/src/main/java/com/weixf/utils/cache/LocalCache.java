package com.weixf.utils.cache;

import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @since 2022-05-31
 */
public class LocalCache {

    /**
     * 默认缓存时长 单位s
     */
    private static final int DEFAULT_TIMEOUT = 3600;
    /**
     * 默认缓存容量
     */
    private static final int DEFAULT_SIZE = 1000;

    /**
     * 存储数据
     */
    private static final Map<String, Object> data;

    /**
     * 定时器  用来控制 缓存的超时时间
     * private static Timer timer;
     * timer = new Timer();
     * timer.schedule(CacheCleanTask.cacheTask(key),DEFAULT_TIMEOUT);
     * <p>
     * 1)多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，
     * 其它任务便会自动终止运行，使用ScheduledExecutorService则没有这个问题
     * 2)Timer内部是一个线程，任务1所需的时间超过了两个任务间的间隔时会导致问题
     * 3)Timer执行周期任务时依赖系统时间
     */
    private static final ScheduledExecutorService executorService;

    // 初始化
    static {
        data = new ConcurrentHashMap<>(DEFAULT_SIZE);
        executorService = new ScheduledThreadPoolExecutor(2);
    }

    /**
     * 私有化构造函数
     */
    private LocalCache() {
    }

    /**
     * 增加缓存 默认有效时长
     */
    public static void put(String key, Object value) {
        data.put(key, value);
        // 定时器 调度任务，用于根据 时间 定时清除 对应key 缓存
        executorService.schedule(new TimerTask() {
            @Override
            public void run() {
                remove(key);
            }
        }, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 增加缓存  并设置缓存时长 单位 s
     *
     * @param timeout 缓存时长 单位s
     */
    public static void put(String key, Object value, int timeout) {
        data.put(key, value);
        // lambda 替换匿名内部类
        executorService.schedule(() -> remove(key), timeout, TimeUnit.SECONDS);
    }

    /**
     * 增加缓存 并指定过期时间点
     *
     * @param expireTime 指定过期时间点
     */
    public static void put(String key, Object value, LocalDateTime expireTime) {
        data.put(key, value);
        LocalDateTime nowTime = LocalDateTime.now();
        if (nowTime.isAfter(expireTime)) {
            // 时间设置异常 待处理
            return;
        }
        long seconds = Duration.between(nowTime, expireTime).getSeconds();
        executorService.schedule(() -> remove(key), seconds, TimeUnit.SECONDS);
    }

    /**
     * 批量增加缓存
     */
    public static void put(Map<String, Object> cache) {
        if (!CollectionUtils.isEmpty(cache)) {
            cache.forEach(LocalCache::put);
        }
    }

    public static void put(Map<String, Object> cache, int timeout) {
        if (!CollectionUtils.isEmpty(cache)) {
            cache.forEach((key, value) -> put(key, value, timeout));
        }
    }

    public static void put(Map<String, Object> cache, LocalDateTime expireTime) {
        if (!CollectionUtils.isEmpty(cache)) {
            cache.forEach((key, value) -> put(key, value, expireTime));
        }
    }

    /**
     * 获取缓存
     */
    public static Object get(String key) {
        return data.get(key);
    }

    /**
     * 获取当前缓存中 所有的key
     */
    public static Set<String> cacheKeys() {
        return data.keySet();
    }

    public static Map<String, Object> allCache() {
        return data;
    }

    /**
     * 获取当前缓存大小
     */
    public static int size() {
        return data.size();
    }

    /**
     * 删除缓存
     */
    public static void remove(String key) {
        data.remove(key);
    }

    /**
     * 清空所有缓存
     */
    public static void clear() {
        if (size() > 0) {
            data.clear();
        }
    }

    /**
     * 判断缓存是否包含key
     */
    public boolean containKey(String key) {
        return data.containsKey(key);
    }
}
