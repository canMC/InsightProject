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
//import utils.BirdFeedConfigurations;
import types.Deal;


public class Producer {

    private static KafkaProducer<String, String> producer;
    private static String[] data;
    
    private static Random random = new Random();
    private static int mean;
    private static int stddev;
    
    public static void main(String[] args) throws IOException {
        mean = data.length/2;
        stddev = data.length/6;
        
       };

}
