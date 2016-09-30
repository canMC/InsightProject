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
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig

import types.{Tweet, Deal}
import utils.{TweetSchema, DealSchema, FlinkRedisMapper}

object KafkaFlinkConsumer extends App {

 val env = StreamExecutionEnvironment.getExecutionEnvironment
 //  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
 env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

 val kafkaProps = new Properties()
 kafkaProps.setProperty("bootstrap.servers", "ec2-52-38-52-141.us-west-2.compute.amazonaws.com:9092")
//  kafkaProps.setProperty("bootstrap.servers", "ec2-54-70-192-13.us-west-2.compute.amazonaws.com:9092")
 //kafkaProps.setProperty("zookeeper.connect", "ec2-52-38-52-141.us-west-2.compute.amazonaws.com:2181")
 kafkaProps.setProperty("group.id", "flink")


  val kafkaConsumerTwitter = new FlinkKafkaConsumer09[Tweet](
   	"twitter-topic",
   	new TweetSchema(),
   kafkaProps
  )

  val kafkaConsumerExpedia = new FlinkKafkaConsumer09[Deal](
    "expedia-topic",
    new DealSchema(),
    kafkaProps
  )


 val streamTwitter = env.addSource(kafkaConsumerTwitter)

 val mappedstreamTwitter = streamTwitter

 //val mappedstreamTwitterT = mappedstreamTwitter.assignTimestampsAndWatermarks(new KafkaFlinkConsumer.timestampExtractorT)
 val mappedstreamTwitterT = mappedstreamTwitter.keyBy(_.location)
 //mappedstreamTwitterT print

 val streamExpedia = env.addSource(kafkaConsumerExpedia)
 val mappedstreamExpedia = streamExpedia
 	.broadcast

 //val mappedstreamExpediaT = mappedstreamExpedia.assignTimestampsAndWatermarks(new KafkaFlinkConsumer.timestampExtractorE)
 val mappedstreamExpediaT = mappedstreamExpedia
 //mappedstreamExpediaT print

  val conf = new FlinkJedisPoolConfig.Builder().setHost("172.31.1.44").build()

 val tw = mappedstreamTwitterT.join(mappedstreamExpediaT)
 	.where(_.location)
 	.equalTo(_.departureAirportLocation)
 	//.window(TumblingEventTimeWindows.of(Time.of(3, TimeUnit.SECONDS)))
 	.window(TumblingProcessingTimeWindows.of(Time.of(3, TimeUnit.SECONDS)))
 	.apply {(tweet: Tweet,
		deal: Deal) => "{\"tweet\":" + tweet.toString() + ",\"deal\":" + deal.toString() + "}"
  		}
   .addSink(new RedisSink[String](conf, new FlinkRedisMapper))

 env.execute("KafkaFlinkConsumer")

}
