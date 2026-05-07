package io.github.hcisme.cowrite.redis

import jakarta.annotation.Resource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisUtils {
    @Resource
    private lateinit var redisConfigTemplate: RedisTemplate<String, Any>

    private val logger: Logger = LoggerFactory.getLogger(RedisUtils::class.java)

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    fun getValue(key: String?): Any? {
        return if (key == null) null else redisConfigTemplate.opsForValue()[key]
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    fun setValue(key: String, value: Any): Boolean {
        return try {
            redisConfigTemplate.opsForValue()[key] = value
            true
        } catch (e: Exception) {
            logger.error("设置redis key {}, value {} 失败", key, value)
            false
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(ms) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    fun setValueAndExpire(key: String, value: Any, time: Long): Boolean {
        return try {
            if (time > 0) {
                redisConfigTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS)
            } else {
                setValue(key, value)
            }
            true
        } catch (e: Exception) {
            logger.error("设置redis key {}, value {} 失败", key, value)
            false
        }
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(ms)
     */
    fun setExpire(key: String, time: Long): Boolean {
        return try {
            if (time > 0) {
                redisConfigTemplate.expire(key, time, TimeUnit.MILLISECONDS)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传 一个值 或 多个
     */
    fun delete(vararg key: String) {
        if (key.isNotEmpty()) {
            if (key.size == 1) {
                redisConfigTemplate.delete(key[0])
            } else {
                redisConfigTemplate.delete(key.asList())
            }
        }
    }

    /**
     * 移除值为value的
     *
     * @param key   键
     * @param value 值
     * @return 移除的个数
     */
    fun remove(key: String, value: Any): Long {
        return try {
            redisConfigTemplate.opsForList().remove(key, 1, value) ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun keyExists(key: String): Boolean {
        return redisConfigTemplate.hasKey(key)
    }
}