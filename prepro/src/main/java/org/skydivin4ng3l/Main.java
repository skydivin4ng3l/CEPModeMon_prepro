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

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.bptlab.cepta.models.events.event.EventOuterClass;
import org.bptlab.cepta.models.events.info.LocationDataOuterClass;
import org.bptlab.cepta.models.events.train.LiveTrainDataOuterClass;
import org.bptlab.cepta.models.events.train.PlannedTrainDataOuterClass;
import org.bptlab.cepta.models.events.event.EventOuterClass.Event;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.kafka.common.serialization.LongSerializer;
import org.skydivin4ng3l.cepmodemon.config.KafkaConfig;
import org.skydivin4ng3l.cepmodemon.operators.BasicCounter;
import org.skydivin4ng3l.cepmodemon.serialization.GenericBinaryProtoDeserializer;
import org.skydivin4ng3l.cepmodemon.serialization.GenericBinaryProtoSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Command(
		name = "cepmodemon prepro",
		mixinStandardHelpOptions = true,
		version = "0.1.0",
		description = "PreProcesses the cepta events coming from the Kafka Monitoring queue.")
public class Main implements Callable<Integer> {

	private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

	// Consumers
	private ArrayList<FlinkKafkaConsumer011<EventOuterClass.Event>> flinkKafkaConsumer011s = new ArrayList<>();

	/*-------------------------
	 * Begin - Monitoring Producers
	 * ------------------------*/
	// Producers
	private ArrayList<FlinkKafkaProducer011<EventOuterClass.Event>> flinkKafkaProducer011s = new ArrayList<>();

	/*-------------------------
	 * End - Monitoring Producers
	 * ------------------------*/
	private void setupConsumers() {
		//get All Kafka Topics
		Map<String, List<PartitionInfo> > topics;

		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "test-consumer-group");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<String, String> topicConsumer = new KafkaConsumer<String, String>(props);
		topics = topicConsumer.listTopics();
		topicConsumer.close();

		for (Map.Entry<String,List<PartitionInfo>> entry : topics.entrySet()) {
			if (entry.getKey().startsWith("MONITOR_")){
				FlinkKafkaConsumer011<EventOuterClass.Event> consumer =
						new FlinkKafkaConsumer011<Event>(entry.getKey(),
								new GenericBinaryProtoDeserializer<Event>(Event.class),
								new KafkaConfig().withClientId(entry.getKey()+"MainConsumer").withGroupID("Monitoring").getProperties());
				this.flinkKafkaConsumer011s.add(consumer);
			}
		}

//		this.liveTrainDataConsumer =
//				new FlinkKafkaConsumer011<>(
//						Topic.LIVE_TRAIN_DATA.getValueDescriptor().getName(),
//						new GenericBinaryProtoDeserializer<EventOuterClass.Event>(EventOuterClass.Event.class),
//						new KafkaConfig().withClientId("LiveTrainDataMainConsumer").getProperties());
//		this.plannedTrainDataConsumer =
//				new FlinkKafkaConsumer011<>(
//						Topic.PLANNED_TRAIN_DATA.getValueDescriptor().getName(),
//						new GenericBinaryProtoDeserializer<EventOuterClass.Event>(EventOuterClass.Event.class),
//						new KafkaConfig().withClientId("PlannedTrainDataMainConsumer").getProperties());
//
//		this.weatherDataConsumer =
//				new FlinkKafkaConsumer011<>(
//						Topic.WEATHER_DATA.getValueDescriptor().getName(),
//						new GenericBinaryProtoDeserializer<EventOuterClass.Event>(EventOuterClass.Event.class),
//						new KafkaConfig().withClientId("WeatherDataMainConsumer").withGroupID("Group").getProperties());
//
//		this.locationDataConsumer =
//				new FlinkKafkaConsumer011<>(
//						Topic.LOCATION_DATA.getValueDescriptor().getName(),
//						new GenericBinaryProtoDeserializer<EventOuterClass.Event>(EventOuterClass.Event.class),
//						new KafkaConfig().withClientId("LocationDataMainConsumer").getProperties());
	}

	private void setupProducers() {


//		KafkaConfig delaySenderConfig = new KafkaConfig().withClientId("TrainDelayNotificationProducer")
//				.withKeySerializer(Optional.of(LongSerializer::new));
//		this.trainDelayNotificationProducer = new FlinkKafkaProducer011<>(
//				Topic.DELAY_NOTIFICATIONS.getValueDescriptor().getName(),
//				new GenericBinaryProtoSerializer<>(),
//				delaySenderConfig.getProperties());
//		this.trainDelayNotificationProducer.setWriteTimestampToKafka(true);
	}

	@Mixin
	KafkaConfig kafkaConfig = new KafkaConfig();


	@Override
	public Integer call() throws Exception {
		logger.info("Starting cepModeMon prepro...");

		// Setup the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1);
		env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
		this.setupConsumers();
		this.setupProducers();


		for ( FlinkKafkaConsumer011<Event> consumer : this.flinkKafkaConsumer011s) {
			DataStream<Event> someEntryStream = env.addSource(consumer);
			someEntryStream.print();
			DataStream<Long> aggregatedStream = someEntryStream.windowAll(TumblingEventTimeWindows.of(Time.seconds(5))).aggregate(new BasicCounter<Event>());
			aggregatedStream.print();
//			DataStream<PlannedTrainDataOuterClass.PlannedTrainData> plannedTrainDataStream = someEntryStream.map(new MapFunction<Event, PlannedTrainDataOuterClass.PlannedTrainData>(){
//				@Override
//				public PlannedTrainDataOuterClass.PlannedTrainData map(Event event) throws Exception{
//					return event.getPlannedTrain();
//				}
//			});
		}

		env.execute("CEPMODEMON PREPRO");
		return 0;
	}

	public static void main(String... args) {
		int exitCode = new CommandLine(new Main()).execute(args);
		System.exit(exitCode);
	}
}
