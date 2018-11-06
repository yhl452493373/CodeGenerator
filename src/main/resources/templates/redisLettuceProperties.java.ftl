package ${cfg.packageRedis};

public class RedisLettuceProperties {
    private RedisLettucePoolProperties pool;

    public RedisLettucePoolProperties getPool() {
        return pool;
    }

    public void setPool(RedisLettucePoolProperties pool) {
        this.pool = pool;
    }
}
