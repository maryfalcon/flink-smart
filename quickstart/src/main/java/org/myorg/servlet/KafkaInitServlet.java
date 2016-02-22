package org.myorg.servlet;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer082;
import org.myorg.dto.UserFileDto;
import org.myorg.serialization.schema.UserFileSchema;
import org.myorg.service.DataService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import java.util.concurrent.Executor;
import org.myorg.cassandra.Data;

/**
 * User: NotePad.by
 * Date: 2/7/2016.
 */
public class KafkaInitServlet extends HttpServlet implements Serializable {
    private static final long serialVersionUID = -4820467108172468488L;

    public static final String TOPIC_PROPERTY = "topic";

    @Override
    public void init() throws ServletException {
        new ThreadPerTaskExecutor().execute(new Runnable() {

            @Override
            public void run() {
                StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
                Properties properties = new Properties();
                try {
                    properties.load(KafkaInitServlet.class.getResourceAsStream("/application.properties"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final DataStream<UserFileDto> messageStream = env.addSource(new FlinkKafkaConsumer082<>(properties.getProperty(TOPIC_PROPERTY),
                        new UserFileSchema(), properties));
                messageStream.map(new MapFunction<UserFileDto, String>() {
                    private static final long serialVersionUID = -6867736771747690202L;
                    @Override
                    public String map(UserFileDto userFileDto) throws Exception {
                        DataService dataService = new DataService();
                        long currentMilliseconds = System.currentTimeMillis();
                        Data savedData = dataService.saveData(userFileDto);
                        return currentMilliseconds + " Kafka and Flink says: " + savedData.getId();
                    }
                }).print();
                try {
                    env.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class ThreadPerTaskExecutor implements Executor {
        public void execute(Runnable r) {
            new Thread(r).start();
        }
    }

}
