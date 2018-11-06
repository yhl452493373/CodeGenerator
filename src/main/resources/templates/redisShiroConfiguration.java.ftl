package ${cfg.packageShiro}.redis;

@SuppressWarnings("WeakerAccess")
public class RedisShiroConfiguration {

    private String sessionPrefix = "shiro-session";

    private int sessionTime = 60;

    private String cachePrefix = "shiro-cache";

    private int cacheTime = 60;

    public String getSessionPrefix() {
        return sessionPrefix;
    }

    public void setSessionPrefix(String sessionPrefix) {
        this.sessionPrefix = sessionPrefix;
    }

    public String getCachePrefix() {
        return cachePrefix;
    }

    public void setCachePrefix(String cachePrefix) {
        this.cachePrefix = cachePrefix;
    }

    public int getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(int sessionTime) {
        this.sessionTime = sessionTime;
    }

    public int getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(int cacheTime) {
        this.cacheTime = cacheTime;
    }

}
