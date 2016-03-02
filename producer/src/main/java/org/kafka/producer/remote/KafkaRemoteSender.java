package org.kafka.producer.remote;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.kafka.producer.dto.UserFileDto;
import org.kafka.producer.serialization.generator.UserFileKafkaGenerator;
import org.kafka.producer.serialization.schema.UserFileSchema;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class KafkaRemoteSender {

    private static final Logger logger = Logger.getLogger(KafkaRemoteSender.class.getName());

    public static final String SERVERS_PROPERTY = "bootstrap.servers";
    public static final String TOPIC_PROPERTY = "topic";

    private Properties properties = new Properties();

    private StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    public KafkaRemoteSender() {
        try {
            properties.load(KafkaRemoteSender.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public void sendFileData(UserFileDto userFileDto) throws Exception {
        DataStream<UserFileDto> messageStream = env.addSource(new UserFileKafkaGenerator(userFileDto));
        messageStream.addSink(new FlinkKafkaProducer<>(properties.getProperty(SERVERS_PROPERTY),
                properties.getProperty(TOPIC_PROPERTY),
                new UserFileSchema()));
        env.execute();
    }

}
