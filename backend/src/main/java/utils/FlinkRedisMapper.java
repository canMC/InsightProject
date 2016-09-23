package utils;

import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

/**
 * Created by milena on 9/20/16.
 */
public class FlinkRedisMapper implements RedisMapper<String>{

    @Override
    public RedisCommandDescription getCommandDescription() {
        return new RedisCommandDescription(RedisCommand.PUBLISH, "PUBLISH_TEST");
    }

    @Override
    public String getKeyFromData(String data) {
        return "dealsForUsers";
    }

    @Override
    public String getValueFromData(String data) {
        return data;
    }
}