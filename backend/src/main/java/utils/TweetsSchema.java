package utils;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.SerializationSchema;
import types.Tweets;

public class TweetsSchema implements DeserializationSchema<Tweets>, SerializationSchema<Tweets> {

    @Override
    public byte[] serialize(Tweets tweet) {
        return tweet.toString().getBytes();
    }

    @Override
    public Tweets deserialize(byte[] record) {
        return Tweets.fromString(new String(record));
    }

    @Override
    public boolean isEndOfStream(Tweets nextTweet) {
        return false;
    }

    @Override
    public TypeInformation<Tweets> getProducedType() {
        return TypeExtractor.getForClass(Tweets.class);
    }
}
