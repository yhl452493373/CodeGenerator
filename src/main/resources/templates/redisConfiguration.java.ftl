package ${cfg.packageRedis};

@SuppressWarnings("WeakerAccess")
public class RedisConfiguration {

    private String cachePrefix = "redis-cache";

    private int cacheTime = 60;

    public String getCachePrefix() {
        return cachePrefix;
    }

    public void setCachePrefix(String cachePrefix) {
        this.cachePrefix = cachePrefix;
    }

    public int getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(int cacheTime) {
        this.cacheTime = cacheTime;
    }
}
