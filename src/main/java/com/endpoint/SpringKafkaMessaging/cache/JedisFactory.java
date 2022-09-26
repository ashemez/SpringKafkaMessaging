package com.endpoint.SpringKafkaMessaging.cache;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class JedisFactory {

    @Value("${cache.redis.host}")
    private static String host = "localhost";

    @Value("${cache.redis.port}")
    private static Integer port = 6379;

    @Value("${cache.redis.timeout}")
    private static Integer timeout = 5000;

    @Value("${cache.redis.password}")
    private static String password = "1234";

    // hide the constructor
    private JedisFactory() {

    }

    private static JedisPool jedisPool;

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);

        jedisPool = new JedisPool(
                poolConfig,
                host,
                port,
                timeout,
                password
        );
    }

    public static Jedis getConnection() {
        return jedisPool.getResource();
    }
}
