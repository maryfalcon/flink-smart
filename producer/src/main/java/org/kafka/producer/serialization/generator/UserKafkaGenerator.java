package org.kafka.producer.serialization.generator;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.kafka.producer.dto.UserFileDto;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class UserKafkaGenerator implements SourceFunction<UserFileDto> {
    @Override
    public void run(SourceContext<UserFileDto> sourceContext) throws Exception {

    }

    @Override
    public void cancel() {

    }
}
