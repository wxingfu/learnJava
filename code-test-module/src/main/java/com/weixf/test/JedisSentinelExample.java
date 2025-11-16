package com.weixf.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public class JedisSentinelExample {

    public static void main(String[] args) {

        // 配置 Sentinel 地址
        Set<String> sentinels = new HashSet<>();
        sentinels.add("10.2.0.192:26379");
        // sentinels.add("10.2.0.193:26379");
        // sentinels.add("10.2.0.194:26379");

        // 创建连接池，指定主节点名称和密码
        try (JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels, "nAnVoLszypA45yZu")) {
            try (Jedis jedis = pool.getResource()) {
                // 测试命令
                String pong = jedis.ping();
                System.out.println("PING: " + pong);

                // 设置和获取键值
                jedis.set("key", "value");
                String value = jedis.get("key");
                System.out.println("GET key: " + value);
            }
        } catch (Exception e) {
            System.err.println("Jedis error: " + e.getMessage());
        }
    }
}
