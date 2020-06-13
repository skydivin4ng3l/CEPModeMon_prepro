package org.skydivin4ng3l.cepmodemon.serialization;

import com.google.common.primitives.Longs;
import org.apache.flink.api.common.serialization.SerializationSchema;

public class SimpleLongSerializer implements SerializationSchema<Long> {
    private String topic;

    public SimpleLongSerializer(String topic) {
        super();
        this.topic = topic;
    }

    @Override
    public byte[] serialize(Long element) {
        return Longs.toByteArray(element);
    }

}
