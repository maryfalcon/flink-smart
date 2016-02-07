package com.example.skiselyov.myapplication.com.example.notepadby.myapplication.serialization.generator;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class StringKafkaGenerator implements SourceFunction<String> {
    private static final long serialVersionUID = 2174904787118597072L;
    boolean running = true;
    long i = 0;
    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        while(running) {
            ctx.collect("element teste:"+ (i++));
            //Thread.sleep(10);
            if (i == 10000) {
                running = false;
            }

        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}