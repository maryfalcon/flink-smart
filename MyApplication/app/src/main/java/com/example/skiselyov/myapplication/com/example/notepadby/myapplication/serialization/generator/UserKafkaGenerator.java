package com.example.skiselyov.myapplication.com.example.notepadby.myapplication.serialization.generator;

import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.dto.UserFileDto;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

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
