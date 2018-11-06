/*
 * Copyright (c) 2017-2018.  放牛极客<l_iupeiyu@qq.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * </p>
 *
 */

package ${cfg.packageShiro}.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked", "WeakerAccess"})
public class RedisShiroCache<K, V> implements Cache<K, V> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RedisShiroConfiguration redisConfiguration;

    private String cacheKey;

    private RedisTemplate<K, V> redisTemplate;

    @SuppressWarnings("rawtypes")
    public RedisShiroCache(String name, RedisTemplate client, RedisShiroConfiguration redisConfiguration) {
        this.redisConfiguration = redisConfiguration;
        this.cacheKey = this.redisConfiguration.getCachePrefix() + ":" + name;
        this.redisTemplate = client;
    }

    @Override
    public V get(K key) throws CacheException {
        logger.info("根据key从Redis中获取对象 key [" + key + "]");
        try {
            if (key == null) {
                return null;
            } else {
                redisTemplate.boundValueOps(getCacheKey(key)).expire(redisConfiguration.getCacheTime(), TimeUnit.SECONDS);
                V v = redisTemplate.boundValueOps(getCacheKey(key)).get();
                return v;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V put(K key, V value) throws CacheException {
        logger.info("根据key从Redis存储 key [" + key + "]");
        try {
            V v = get(key);
            redisTemplate.boundValueOps(getCacheKey(key)).set(value, redisConfiguration.getCacheTime(), TimeUnit.SECONDS);
            System.out.println(redisTemplate.boundValueOps(getCacheKey(key)).get());
            return v;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        logger.info("从redis中删除 key [" + key + "]");
        try {
            V v = get(key);
            redisTemplate.delete(getCacheKey(key));
            return v;
        } catch (Throwable t) {
            throw new CacheException(t);
        }

    }

    @Override
    public void clear() throws CacheException {
        try {
            redisTemplate.delete(keys());
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        return redisTemplate.keys(getCacheKey("*"));
    }

    @Override
    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<>();
        for (K s : set) {
            list.add(get(s));
        }
        return list;
    }

    private K getCacheKey(Object k) {
        return (K) (this.cacheKey + ":" + k);
    }
}