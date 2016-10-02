package org.apache.flink.streaming.api.scala

import java.util.concurrent.TimeUnit
import java.util.Properties
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig

import types.{Tweet, Deal}
import utils.{TweetSchema, DealSchema, FlinkRedisMapper}

object KafkaFlinkConsumer extends App {

  //specify time characteristics
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)

  //specify the public DNS address of one of the Kafka broker
  val kafkaProps = new Properties()
  kafkaProps.setProperty("bootstrap.servers", "ec2-52-38-52-141.us-west-2.compute.amazonaws.com:9092")
  //the following line can be omitted in FlinkKafkaConsumer09 version
  //kafkaProps.setProperty("zookeeper.connect", "ec2-52-38-52-141.us-west-2.compute.amazonaws.com:2181")
  kafkaProps.setProperty("group.id", "flink")

  //read Twitter stream from Kafka's "twitter-topic" and specify DeserializationSchema
  val kafkaConsumerTwitter = new FlinkKafkaConsumer09[Tweet](
    "twitter-topic",
    new TweetSchema(),
    kafkaProps
  )

  //read Expedia stream from Kafka's "expedia-topic" and specify DeserializationSchema
  val kafkaConsumerExpedia = new FlinkKafkaConsumer09[Deal](
    "expedia-topic",
    new DealSchema(),
    kafkaProps
  )

  val streamTwitter = env.addSource(kafkaConsumerTwitter)

  //Logically partitions a stream into disjoint partitions by key
  val streamTwitterT = streamTwitter
    .keyBy(_.location)

  val streamExpedia = env.addSource(kafkaConsumerExpedia)

  //Replicate Expedia stream elements to every partition using broadcast
  val streamExpediaT = streamExpedia
    .broadcast

  //Configure Flink to Redis connector
  val conf = new FlinkJedisPoolConfig.Builder().setHost("172.31.1.44").build()

  //Join two streams and sink the result to Redis
  val tw = streamTwitterT.join(streamExpediaT)
    .where(_.location)
    .equalTo(_.departureAirportLocation)
    .window(TumblingProcessingTimeWindows.of(Time.of(3, TimeUnit.SECONDS)))
    .apply { (tweet: Tweet,
              deal: Deal) => "{\"tweet\":" + tweet.toString() + ",\"deal\":" + deal.toString() + "}"
    }
    .addSink(new RedisSink[String](conf, new FlinkRedisMapper))

  env.execute("KafkaFlinkConsumer")

}
