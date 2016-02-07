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
package org.myorg.kafka;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer082;

import java.util.Properties;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.myorg.model.Data;
import org.myorg.persistor.Persistor;
import org.myorg.quickstart.FlinkApp;
import org.myorg.service.DataService;

/**
 * Simple example on how to read with a Kafka consumer
 *
 * Note that the Kafka source is expecting the following parameters to be set -
 * "bootstrap.servers" (comma separated list of kafka brokers) -
 * "zookeeper.connect" (comma separated list of zookeeper servers) - "group.id"
 * the id of the consumer group - "topic" the name of the topic to read data
 * from.
 *
 * You can pass these required parameters using "--bootstrap.servers
 * host:port,host1:port1 --zookeeper.connect host:port --topic testTopic"
 *
 * This is a valid input example: --topic test --bootstrap.servers
 * localhost:9092 --zookeeper.connect localhost:2181 --group.id myGroup
 *
 *
 */
public class ReadFromKafka {

    public static final String SERVERS_PROPERTY = "bootstrap.servers";
    public static final String TOPIC_PROPERTY = "topic";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.load(ReadFromKafka.class.getResourceAsStream("/application.properties"));

        DataStream<UserFileDto> messageStream = env.addSource(new FlinkKafkaConsumer082<>(properties.getProperty(TOPIC_PROPERTY),
                new UserFileSchema(), properties));
        final Persistor peristor = new Persistor();

        messageStream.map(new MapFunction<UserFileDto, String>() {
            private static final long serialVersionUID = -6867736771747690202L;

            @Override
            public String map(UserFileDto value) throws Exception {
                long currentMilliseconds = System.currentTimeMillis();
                System.out.println(currentMilliseconds + " Kafka and Flink says: " + value);
                return currentMilliseconds + " Kafka and Flink says: " + value;
            }
        }).print();

        messageStream.addSink(new SinkFunction<UserFileDto>() {
            @Override
            public void invoke(UserFileDto value) throws Exception {
                Data data = new Data(value.getFileName(), value.getFile(), value.getExtension(), 2, value.getDate(), value.getPlace());
                data = new Persistor().insertData(data);
                FlinkApp.sendFileSignature(data.getId(), value.getSignature(), value.getDateHash(), value.getPlaceHash());
                //FlinkApp.checkFileSignature(data.getId(), value.getSignature(), value.getDateHash(), value.getPlaceHash());
            }
        });
        env.execute();

    }
}
