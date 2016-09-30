import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executors;

import com.esotericsoftware.yamlbeans.YamlReader;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import types.Deal;

//TODO - this class is not used and only a placeholder
public class Producer {

    private static KafkaProducer<String, String> producer;
    private static String[] data;
    
    public static void main(String[] args) throws IOException {
        
       };

}
