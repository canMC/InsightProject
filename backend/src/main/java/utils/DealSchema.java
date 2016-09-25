package utils;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.SerializationSchema;
import types.Deal;

public class DealSchema implements DeserializationSchema<Deal>, SerializationSchema<Deal> {

    @Override
    public byte[] serialize(Deal deal) {
        return deal.toString().getBytes();
    }

    @Override
    public Deal deserialize(byte[] record) {
        return Deal.fromString(new String(record));
    }

    @Override
    public boolean isEndOfStream(Deal nextDeal) {
        return false;
    }

    @Override
    public TypeInformation<Deal> getProducedType() {
        return TypeExtractor.getForClass(Deal.class);
    }
}
