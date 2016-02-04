package org.kafka.producer.serialization.generator;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.kafka.producer.dto.UserFileDto;

import java.util.List;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class UserFileKafkaGenerator implements SourceFunction<UserFileDto> {

    public UserFileKafkaGenerator(List<UserFileDto> userFileDtoList) {
        this.userFileDtos = userFileDtoList;
    }

    private UserFileKafkaGenerator() {};

    private List<UserFileDto> userFileDtos;

    private boolean running = true;

    @Override
    public void run(SourceContext<UserFileDto> sourceContext) throws Exception {
        if (running) {
            for (UserFileDto userFileDto : userFileDtos) {
                sourceContext.collect(userFileDto);
            }
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
