import org.apache.flink.streaming.api.scala.KafkaFlinkConsumer;

public class Run {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) throw new IllegalArgumentException("Must specify KafkaFlinkCOnsumer");

        KafkaFlinkConsumer.main(args);
    }
}
