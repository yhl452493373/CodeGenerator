package ${cfg.packageRedis};

import java.time.Duration;

public class RedisProperties {
    private String host;
    private Integer port;
    private Integer database;
    private String password;
    private Duration timeout;
    private RedisLettuceProperties lettuce;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public RedisLettuceProperties getLettuce() {
        return lettuce;
    }

    public void setLettuce(RedisLettuceProperties lettuce) {
        this.lettuce = lettuce;
    }
}
