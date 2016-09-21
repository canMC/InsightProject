package utils;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.SerializationSchema;
import types.Deals;

public class DealsSchema implements DeserializationSchema<Deals>, SerializationSchema<Deals> {

    @Override
    public byte[] serialize(Deals deal) {
        return deal.toString().getBytes();
    }

    @Override
    public Deals deserialize(byte[] record) {
        return Deals.fromString(new String(record));
    }

    @Override
    public boolean isEndOfStream(Deals nextDeal) {
        return false;
    }

    @Override
    public TypeInformation<Deals> getProducedType() {
        return TypeExtractor.getForClass(Deals.class);
    }
}
