/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.skydivin4ng3l.cepmodemon;

import java.util.*;
import java.util.concurrent.Callable;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.bptlab.cepta.models.monitoring.monitor.MonitorOuterClass;
import org.skydivin4ng3l.cepmodemon.config.KafkaConfig;
import org.skydivin4ng3l.cepmodemon.models.events.aggregate.AggregateOuterClass;
import org.skydivin4ng3l.cepmodemon.operators.BasicCounter;
import org.skydivin4ng3l.cepmodemon.operators.EmptyEventSource;
import org.skydivin4ng3l.cepmodemon.serialization.GenericBinaryProtoDeserializer;
import org.skydivin4ng3l.cepmodemon.serialization.GenericBinaryProtoSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(
		name = "cepmodemon prepro",
		mixinStandardHelpOptions = true,
		version = "0.1.9",
		description = "PreProcesses the cepta events coming from the Kafka Monitoring queue.")
public class Main implements Callable<Integer> {

	private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());
	//topics
	private Map<String, List<PartitionInfo> > incomingTopics;
	// Consumers
	private Map<String,FlinkKafkaConsumer011<MonitorOuterClass.Monitor>> flinkKafkaConsumer011s;

	/*-------------------------
	 * Begin - Monitoring Producers
	 * ------------------------*/
	// Producers
	private Map<String,FlinkKafkaProducer011<AggregateOuterClass.Aggregate>> flinkKafkaProducer011s;

	/*-------------------------
	 * End - Monitoring Producers
	 * ------------------------*/
	private void setupConsumers() throws InterruptedException {
		//get All Kafka Topics
		this.flinkKafkaConsumer011s = new HashMap<>();


		Properties props = new Properties();
		props.put("bootstrap.servers", new KafkaConfig().getBroker()/*"localhost:9092"*/);
		props.put("group.id", "test-consumer-group");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");


		KafkaConsumer<String, String> topicConsumer = new KafkaConsumer<String, String>(props);
		incomingTopics = topicConsumer.listTopics();
		System.out.print(incomingTopics);
		topicConsumer.close();

		Iterator<Map.Entry<String, List<PartitionInfo>>> iterator = incomingTopics.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<String, List<PartitionInfo>> entry = iterator.next();
			String topic = entry.getKey();
			if (topic.startsWith("MONITOR_") && !topic.startsWith("MONITOR_AGGREGATED_")) {
				FlinkKafkaConsumer011<MonitorOuterClass.Monitor> consumer =
						new FlinkKafkaConsumer011<MonitorOuterClass.Monitor>(topic,
								new GenericBinaryProtoDeserializer<MonitorOuterClass.Monitor>(MonitorOuterClass.Monitor.class),
								new KafkaConfig().withClientId(topic + "MainConsumer").withGroupID("Monitoring").getProperties());
				this.flinkKafkaConsumer011s.put(topic, consumer);
			} else {
				iterator.remove();
			}
		}
	}

	private void setupProducers() {
		this.flinkKafkaProducer011s = new HashMap<>();

		for (Map.Entry<String,List<PartitionInfo>> entry : incomingTopics.entrySet()) {
			String newOutgoingTopic = entry.getKey().replaceFirst("_","_AGGREGATED_");
			FlinkKafkaProducer011<AggregateOuterClass.Aggregate> producer =
					new FlinkKafkaProducer011<>(newOutgoingTopic,
							new GenericBinaryProtoSerializer<>(),
							new KafkaConfig().withClientId(newOutgoingTopic+"Producer").withGroupID("Monitoring").getProperties());
			producer.setWriteTimestampToKafka(true);
			this.flinkKafkaProducer011s.put(entry.getKey(),producer);
		}
	}

	@Mixin
	KafkaConfig kafkaConfig = new KafkaConfig();


	@Override
	public Integer call() throws Exception {
		logger.info("Starting cepModeMon prepro...");

		Integer currentTry = 0;
		Integer maxTries = 20;
		Long timeBetweenTries = 1000l;
		long delayPerRecordMillis = 5l * 1000l;
		while (true) {

			// Setup the streaming execution environment
			final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
			env.setParallelism(1);
			env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
			this.setupConsumers();
			this.setupProducers();


			for (Map.Entry<String,List<PartitionInfo>> entry : incomingTopics.entrySet()) {
				String currentTopic = entry.getKey();
				FlinkKafkaConsumer011<MonitorOuterClass.Monitor> currentConsumer = flinkKafkaConsumer011s.get(currentTopic);
				FlinkKafkaProducer011<AggregateOuterClass.Aggregate> currentProducer = flinkKafkaProducer011s.get(currentTopic);
				DataStream<MonitorOuterClass.Monitor> someEntryStream = env.addSource(currentConsumer);
				someEntryStream.print();
				DataStream<MonitorOuterClass.Monitor> triggerStream = env.addSource(new EmptyEventSource(delayPerRecordMillis));
				DataStream<AggregateOuterClass.Aggregate> aggregatedStream = someEntryStream
						.union(triggerStream)
						.windowAll(TumblingProcessingTimeWindows.of(Time.seconds(5)))
						.aggregate(new BasicCounter<MonitorOuterClass.Monitor>());
				aggregatedStream.print();
				aggregatedStream.addSink(currentProducer);
			}

			try {
				env.execute("CEPMODEMON PREPRO");
				break;
			} catch (IllegalStateException e) {
				e.printStackTrace();
				if (maxTries<=currentTry) {
					break;
				} else {
					currentTry++;
					Thread.sleep(timeBetweenTries);
					timeBetweenTries *= 2;
					logger.info("Restarting cepModeMon prepro...for the "+currentTry+". Time");
				}
			}
		}

		return 0;
	}

	public static void main(String... args) {
		int exitCode = new CommandLine(new Main()).execute(args);
		System.exit(exitCode);
	}
}
