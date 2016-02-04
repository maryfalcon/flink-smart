package org.kafka.producer.serialization.schema;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.SerializationSchema;
import org.codehaus.jackson.map.ObjectMapper;
import org.kafka.producer.dto.UserDto;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class UserSchema implements DeserializationSchema<UserDto>, SerializationSchema<UserDto, byte[]> {

    private static final Logger logger = Logger.getLogger(UserFileSchema.class.getName());

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public UserDto deserialize(byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, UserDto.class);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean isEndOfStream(UserDto userDto) {
        return false;
    }

    @Override
    public TypeInformation<UserDto> getProducedType() {
        return TypeExtractor.getForClass(UserDto.class);
    }

    @Override
    public byte[] serialize(UserDto userDto) {
        try {
            return objectMapper.writeValueAsBytes(userDto);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }
}
