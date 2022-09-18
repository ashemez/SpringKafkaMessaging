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
    private static String password = "";

    // hide the constructor
    private JedisFactory() {

    }

    private static JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

    private static JedisPool jedisPool;

    static {
        final JedisPoolConfig poolConfig = buildPoolConfig();

        jedisPool = new JedisPool(
            poolConfig,
            host
        );
    }

    public static Jedis getConnection() {
        try {
            return jedisPool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
