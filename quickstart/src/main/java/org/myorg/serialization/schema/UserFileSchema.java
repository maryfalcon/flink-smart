package org.myorg.serialization.schema;


import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.SerializationSchema;
import org.codehaus.jackson.map.ObjectMapper;
import org.myorg.dto.UserFileDto;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class UserFileSchema implements DeserializationSchema<UserFileDto>, SerializationSchema<UserFileDto, byte[]> {

    private static final Logger logger = Logger.getLogger(UserFileSchema.class.getName());

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public UserFileDto deserialize(byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, UserFileDto.class);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean isEndOfStream(UserFileDto userFileDto) {
        return false;
    }

    @Override
    public TypeInformation<UserFileDto> getProducedType() {
        return TypeExtractor.getForClass(UserFileDto.class);
    }

    @Override
    public byte[] serialize(UserFileDto userFileDto) {
        try {
            return objectMapper.writeValueAsBytes(userFileDto);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }
}
