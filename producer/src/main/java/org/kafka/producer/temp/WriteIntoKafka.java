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
package org.kafka.producer.temp;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.KafkaSink;
import org.kafka.producer.dto.UserFileDto;
import org.kafka.producer.serialization.schema.UserFileSchema;
import org.kafka.producer.serialization.generator.UserFileKafkaGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Simple example for writing data into Kafka.
 *
 * The following arguments are required:
 *
 *  - "bootstrap.servers" (comma separated list of kafka brokers)
 *  - "topic" the name of the topic to write data to.
 *
 * This is an example command line argument:
 *  "--topic test --bootstrap.servers localhost:9092"
 */
public class WriteIntoKafka {

    public static final String SERVERS_PROPERTY = "bootstrap.servers";
    public static final String TOPIC_PROPERTY = "topic";

	public static void main(String[] args) throws Exception {
		// create execution environment
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// parse user parameters
        Properties properties = new Properties();
        properties.load(WriteIntoKafka.class.getResourceAsStream("/application.properties"));

		// add a simple source which is writing some strings
        List<UserFileDto> userFileDtos = new ArrayList<>();
        userFileDtos.add(new UserFileDto(1L, "SomeName", new byte[] {(byte)3123,(byte)33123,(byte)312321} , null, "png", 1));
		DataStream<UserFileDto> messageStream = env.addSource(new UserFileKafkaGenerator(userFileDtos));

		// write stream to Kafka
		messageStream.addSink(new KafkaSink<>(properties.getProperty(SERVERS_PROPERTY),
				properties.getProperty(TOPIC_PROPERTY),
				new UserFileSchema()));

		//messageStream.writeAsCsv("/home/veith/Documents/return.csv");
		env.execute();
	}

}