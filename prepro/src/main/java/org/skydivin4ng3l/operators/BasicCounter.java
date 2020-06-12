package org.skydivin4ng3l.cepmodemon.operators;

import com.google.protobuf.Message;
import org.apache.flink.api.common.functions.AggregateFunction;

public class BasicCounter<T extends Message> implements AggregateFunction<T, Long, Long> {
    @Override
    public Long createAccumulator() {
        return 0L;
    }

    @Override
    public Long add(T t, Long aLong) {
        return aLong + 1L;
    }

    @Override
    public Long getResult(Long aLong) {
        return aLong;
    }

    @Override
    public Long merge(Long aLong, Long acc1) {
        return aLong + acc1;
    }
}
