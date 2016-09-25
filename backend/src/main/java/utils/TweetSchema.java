package utils;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.SerializationSchema;
import types.Tweet;

public class TweetSchema implements DeserializationSchema<Tweet>, SerializationSchema<Tweet> {

    @Override
    public byte[] serialize(Tweet tweet) {
        return tweet.toString().getBytes();
    }

    @Override
    public Tweet deserialize(byte[] record) {
        return Tweet.fromString(new String(record));
    }

    @Override
    public boolean isEndOfStream(Tweet tweetBird) {
        return false;
    }

    @Override
    public TypeInformation<Tweet> getProducedType() {
        return TypeExtractor.getForClass(Tweet.class);
    }
}
