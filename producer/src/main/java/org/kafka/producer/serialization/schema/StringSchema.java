package org.kafka.producer.serialization.schema;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.SerializationSchema;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class StringSchema implements DeserializationSchema<String>, SerializationSchema<String, byte[]> {
    private static final long serialVersionUID = 1L;

    public StringSchema() {
    }

    public String deserialize(byte[] message) {
        return new String(message);
    }

    public boolean isEndOfStream(String nextElement) {
        return false;
    }

    public byte[] serialize(String element) {
        return element.getBytes();
    }

    public TypeInformation<String> getProducedType() {
        return TypeExtractor.getForClass(String.class);
    }
}