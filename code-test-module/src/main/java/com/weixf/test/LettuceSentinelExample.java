package com.weixf.test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.RedisCommandExecutionException;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class LettuceSentinelExample {
    public static void main(String[] args) {
        // 配置 Sentinel 地址和主节点名称
        RedisURI redisURI = RedisURI.Builder.sentinel("10.2.0.193", 26379, "mymaster")
                .withPassword("nAnVoLszypA45yZu".toCharArray())
                .build();

        RedisClient client = RedisClient.create(redisURI);
        try (StatefulRedisConnection<String, String> connection = client.connect()) {
            RedisCommands<String, String> commands = connection.sync();
            // 测试命令
            String pong = commands.ping();
            System.out.println("PING: " + pong);

            // 设置和获取键值
            commands.set("key", "value");
            String value = commands.get("key");
            System.out.println("GET key: " + value);

        } catch (RedisCommandExecutionException e) {
            System.err.println("Redis command error: " + e.getMessage());
        } finally {
            client.shutdown();
        }
    }
}
