package org.skydivin4ng3l.cepmodemon.operators;

import com.google.protobuf.Message;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.skydivin4ng3l.cepmodemon.models.events.aggregate.AggregateOuterClass;

public class BasicCounter<T extends Message> implements AggregateFunction<T, Long, AggregateOuterClass.Aggregate> {
    @Override
    public Long createAccumulator() {
        return 0L;
    }

    @Override
    public Long add(T event, Long accumulator) {
        return accumulator + 1L;
    }

    @Override
    public AggregateOuterClass.Aggregate getResult(Long accumulator) {
        return AggregateOuterClass.Aggregate.newBuilder().setVolume(accumulator).build();
    }

    @Override
    public Long merge(Long accumulator1, Long accumulator2) {
        return accumulator1 + accumulator2;
    }
}
