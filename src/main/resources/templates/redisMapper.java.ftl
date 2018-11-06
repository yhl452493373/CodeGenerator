package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
import ${cfg.packageRedis}.RedisCache;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
<#if (enableCache&&cfg.enableRedis)>
@CacheNamespace(implementation= RedisCache::class.java,eviction= RedisCache::class.java)
</#if>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
<#if (enableCache&&cfg.enableRedis)>
@CacheNamespace(implementation= RedisCache.class,eviction= RedisCache.class)
</#if>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>
