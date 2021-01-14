package com.endpoint.SpringKafkaMessaging.cache;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisFactory {

    @Value("${cache.redis.host}")
    private static String host;

    @Value("${cache.redis.port}")
    private static Integer port;

    @Value("${cache.redis.timeout}")
    private static Integer timeout;

    @Value("${cache.redis.password}")
    private static String password;

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
