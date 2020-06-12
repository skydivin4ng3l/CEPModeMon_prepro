package org.skydivin4ng3l.cepmodemon.serialization;

import com.google.protobuf.GeneratedMessageV3;
import org.apache.flink.api.common.serialization.SerializationSchema;

public class GenericBinaryProtoSerializer<T extends GeneratedMessageV3> implements SerializationSchema<T> {

  @Override
    public byte[] serialize(T value) {
        return value.toByteArray();
    }
}