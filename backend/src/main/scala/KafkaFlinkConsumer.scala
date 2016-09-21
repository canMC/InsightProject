package org.apache.flink.streaming.api.scala

import java.util.concurrent.TimeUnit
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import java.util.Properties
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
//import org.apache.flink.streaming.util.serialization.DeserializationSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark

import types.{Deals, Tweets}
import utils.{DealsSchema, TweetsSchema}

object KafkaFlinkConsumer extends App {

 case class Tweets(location: String, name:String, timestamp:Int)
 case class Deals(location: String, price: String, timestamp:Int)


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
//val kafkaConsumerTwitter = new FlinkKafkaConsumer09[Tweets](
// 	"twitter-topic",
// 	new TweetsSchema(),
// 	kafkaProps
// 	)


 //val kafkaConsumerExpedia = new FlinkKafkaConsumer09[Deals](
 //	"expedia-topic",
 //	new DealsSchema(),
 //	kafkaProps
 //	)

 val kafkaConsumerExpedia = new FlinkKafkaConsumer09[String](
 	"expedia-topic",
 	new SimpleStringSchema(),
 	kafkaProps
 	)

 val streamTwitter = env.addSource(kafkaConsumerTwitter)
 val mappedstreamTwitter = streamTwitter
 	.map(tweet => {
  	val split = tweet.split(",")
  	Tweets(split(0), split(1),2)
  	})

 //val mappedstreamTwitter = streamTwitter.map(tweet => (tweet, 2 ))

 //val mappedstreamTwitterT = mappedstreamTwitter.assignTimestampsAndWatermarks(new KafkaFlinkConsumer.timestampExtractorT)
 val mappedstreamTwitterT = mappedstreamTwitter
 mappedstreamTwitterT print

 val streamExpedia = env.addSource(kafkaConsumerExpedia)
 val mappedstreamExpedia = streamExpedia
 	.broadcast
 	.map(deal => {
 	val split = deal.split(",")
  	Deals(split(0), split(1),2)
  	})
 
 //val mappedstreamExpediaT = mappedstreamExpedia.assignTimestampsAndWatermarks(new KafkaFlinkConsumer.timestampExtractorE)
 val mappedstreamExpediaT = mappedstreamExpedia
 //mappedstreamExpediaT print

 val tw = mappedstreamTwitterT.join(mappedstreamExpediaT)
 	.where(_.location)
 	.equalTo(_.location)
 	//.window(TumblingEventTimeWindows.of(Time.of(3, TimeUnit.SECONDS)))
 	.window(TumblingProcessingTimeWindows.of(Time.of(3, TimeUnit.SECONDS)))
 	.apply {(tweet: Tweets,
		deal: Deals) => "in " + tweet.location + "->" + tweet.name + " got deal-> " + deal.price
  		}
	.print

 env.execute("KafkaFlinkConsumer")

}
