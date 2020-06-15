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
    public Long add(T t, Long aLong) {
        return aLong + 1L;
    }

    @Override
    public AggregateOuterClass.Aggregate getResult(Long aLong) {
        return AggregateOuterClass.Aggregate.newBuilder().setVolume(aLong).build();
    }

    @Override
    public Long merge(Long aLong, Long acc1) {
        return aLong + acc1;
    }
}
