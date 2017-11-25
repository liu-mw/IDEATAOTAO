package com.taotao.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author liu_mw
 * @date 2017/11/25 11:56
 */
@Service
public class RedisServiceOld {

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    /**
     * 执行set操作
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.set(key, value);
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
    }

    /**
     * 执行get操作
     *
     * @param key
     * @return
     */
    public String get(String key) {
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.get(key);
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
    }

}