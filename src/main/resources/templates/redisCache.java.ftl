package ${cfg.packageRedis};

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class RedisCache implements Cache {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static RedisTemplate<Object, Object> redisTemplate;

    private static RedisConfiguration redisConfiguration;

    private final String id;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public RedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public void clear() {
        redisTemplate.delete(keys());
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Object getObject(Object key) {
        if (key == null) {
            return null;
        } else {
            logger.info("根据key从Redis中获取对象 key [" + key + "]");
            redisTemplate.boundValueOps(getCacheKey(key));
            redisTemplate.boundValueOps(getCacheKey(key)).expire(redisConfiguration.getCacheTime(), TimeUnit.SECONDS);
            Object object = redisTemplate.boundValueOps(getCacheKey(key)).get();
            return object;
        }
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    @Override
    public int getSize() {
        return keys().size();
    }

    @Override
    public void putObject(Object key, Object value) {
        logger.info("根据key从Redis存储 key [" + key + "]");
        redisTemplate.boundValueOps(getCacheKey(key)).set(value, redisConfiguration.getCacheTime(), TimeUnit.SECONDS);
    }

    @Override
    public Object removeObject(Object key) {
        logger.info("从redis中删除 key [" + key + "]");
        return redisTemplate.delete(getCacheKey(key));
    }

    public Set<Object> keys() {
        return redisTemplate.keys(getCacheKey("*"));
    }


    public String getCacheKey(Object key) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        return new String(Objects.requireNonNull(stringRedisSerializer.serialize(redisConfiguration.getCachePrefix() + ":" + key.toString())));
    }

    public static void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        RedisCache.redisTemplate = redisTemplate;
    }

    public static void setRedisConfiguration(RedisConfiguration redisConfiguration) {
        RedisCache.redisConfiguration = redisConfiguration;
    }
}