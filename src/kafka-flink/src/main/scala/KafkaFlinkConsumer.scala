package org.apache.flink.streaming.api.scala

import java.util.concurrent.TimeUnit
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import java.util.Properties
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark

object KafkaFlinkConsumer {

case class Tweets(location: String, name:String, timestamp:Int)
case class Deals(location: String, price: String, timestamp:Int)

    
  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
  //  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime) 
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

    val kafkaProps = new Properties()
    kafkaProps.setProperty("bootstrap.servers", "ec2-52-38-52-141.us-west-2.compute.amazonaws.com:9092")
   //kafkaProps.setProperty("zookeeper.connect", "ec2-52-38-52-141.us-west-2.compute.amazonaws.com:2181")
    kafkaProps.setProperty("group.id", "flink")
 
    val kafkaConsumerTwitter = new FlinkKafkaConsumer09[String](
      "twitter-topic",
      new SimpleStringSchema(),
      kafkaProps
    )


    val kafkaConsumerExpedia = new FlinkKafkaConsumer09[String](
      "expedia-topic",
      new SimpleStringSchema(),
      kafkaProps
    )

    val streamTwitter = env.addSource(kafkaConsumerTwitter)
    val mappedstreamTwitter = streamTwitter.map(tweet => {
    val split = tweet.split(",")
    Tweets(split(0), split(1),2)
  })
 
    //val mappedstreamTwitter = streamTwitter.map(tweet => (tweet, 2 )) 
   
    //val mappedstreamTwitterT = mappedstreamTwitter.assignTimestampsAndWatermarks(new KafkaFlinkConsumer.timestampExtractorT) 
    val mappedstreamTwitterT = mappedstreamTwitter 
   
    mappedstreamTwitterT print

    val streamExpedia = env.addSource(kafkaConsumerExpedia)
    val mappedstreamExpedia = streamExpedia.map(deal => {
    val split = deal.split(",")
    Deals(split(0), split(1),2)
  }) 
    //val mappedstreamExpediaT = mappedstreamExpedia.assignTimestampsAndWatermarks(new KafkaFlinkConsumer.timestampExtractorE)
    val mappedstreamExpediaT = mappedstreamExpedia
    mappedstreamExpediaT print

//val kafkaProducerJoin = new FlinkKafkaProducer09[String](
//      "ec2-52-38-52-141.us-west-2.compute.amazonaws.com:9092",
//      "my-topic",
//      new SimpleStringSchema()
//    )


  val source1 = env.addSource(new SourceFunction[(String, String, Int)]() {
      def run(ctx: SourceFunction.SourceContext[(String, String, Int)]) {
        ctx.collect(("a", "x", 0))
        ctx.collect(("a", "y", 1))
        ctx.collect(("a", "z", 2))

        ctx.collect(("b", "u", 3))
        ctx.collect(("b", "w", 5))

        ctx.collect(("a", "i", 6))
        ctx.collect(("a", "j", 7))
        ctx.collect(("a", "k", 8))

        // source is finite, so it will have an implicit MAX watermark when it finishes
      }

      def cancel() {}
      
    }).assignTimestampsAndWatermarks(new KafkaFlinkConsumer.Tuple3TimestampExtractor)

//val join = source1.join(source1)
//      .where(_._1)
//      .equalTo(_._1)
//      .window(TumblingEventTimeWindows.of(Time.of(3, TimeUnit.MILLISECONDS)))
//      .apply( (l, r) => l.toString + ":got:" + r.toString)
//      .print()



val tw = mappedstreamTwitterT.join(mappedstreamExpediaT)
        .where(_.location)
        .equalTo(_.location)
        //.window(TumblingEventTimeWindows.of(Time.of(3, TimeUnit.SECONDS)))
        .window(TumblingProcessingTimeWindows.of(Time.of(3, TimeUnit.SECONDS)))
        .apply{(tweet: Tweets,
                deal: Deals) => "in " + tweet.location + "->" + tweet.name + " got deal-> " + deal.price
       }.print
env.execute("KafkaFlinkConsumer")
  }

  private class Tuple3TimestampExtractor extends 
        AssignerWithPunctuatedWatermarks[(String, String, Int)] {
    
    override def extractTimestamp(element: (String, String, Int), previousTimestamp: Long): Long
         = element._3

    override def checkAndGetNextWatermark(
        lastElement: (String, String, Int),
        extractedTimestamp: Long): Watermark = new Watermark(extractedTimestamp - 1)
  }

private class timestampExtractorT extends
        AssignerWithPunctuatedWatermarks[Tweets] {

    override def extractTimestamp(tweet: Tweets, previousTimestamp: Long): Long
         = tweet.timestamp 

    override def checkAndGetNextWatermark(
        lastTweet: Tweets ,
        extractedTimestamp: Long): Watermark = new Watermark(extractedTimestamp - 1)
  }
private class timestampExtractorE extends
        AssignerWithPunctuatedWatermarks[Deals] {

    override def extractTimestamp(deal: Deals, previousTimestamp: Long): Long
         = deal.timestamp

    override def checkAndGetNextWatermark(
        lastDeal: Deals ,
        extractedTimestamp: Long): Watermark = new Watermark(extractedTimestamp - 1)
  }
}
