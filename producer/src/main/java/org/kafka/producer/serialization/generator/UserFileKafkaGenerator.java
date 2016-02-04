package org.kafka.producer.serialization.generator;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.kafka.producer.dto.UserFileDto;

import java.util.List;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class UserFileKafkaGenerator implements SourceFunction<UserFileDto> {

    private UserFileDto userFileDto;

    private boolean running = true;

    public UserFileKafkaGenerator(UserFileDto userFileDto) {
        this.userFileDto = userFileDto;
    }

    private UserFileKafkaGenerator() {};

    @Override
    public void run(SourceContext<UserFileDto> sourceContext) throws Exception {
        if (running) {
            sourceContext.collect(userFileDto);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
