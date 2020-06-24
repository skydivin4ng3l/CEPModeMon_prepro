package org.skydivin4ng3l.cepmodemon.operators;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.bptlab.cepta.models.monitoring.monitor.MonitorOuterClass;

public class EmptyEventSource implements SourceFunction<MonitorOuterClass.Monitor> {

    private volatile boolean isRunning = true;

    private final long delayPerRecordMillis;

    public EmptyEventSource(long delayPerRecordMillis){
        this.delayPerRecordMillis = delayPerRecordMillis;
    }

    @Override
    public void run(SourceContext<MonitorOuterClass.Monitor> sourceContext) throws Exception {
        while (isRunning) {
            sourceContext.collect(MonitorOuterClass.Monitor.newBuilder().build());

            if (delayPerRecordMillis > 0) {
                Thread.sleep(delayPerRecordMillis);
            }
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
