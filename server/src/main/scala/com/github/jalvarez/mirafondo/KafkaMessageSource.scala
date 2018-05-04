package com.github.jalvarez.mirafondo

import akka.stream.scaladsl.Source
import akka.util.ByteString
import scala.concurrent.duration._
import MessagePacking.Message
import akka.kafka.scaladsl.Consumer
import org.apache.kafka.common.serialization.StringDeserializer
import akka.kafka.ConsumerSettings
import akka.actor.ActorSystem
import akka.kafka.Subscriptions
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.TopicPartition

class KafkaMessageSource(system: ActorSystem, kafkaServers: String, groupId: String) extends MessageSource {
  lazy val consumerSettings = ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
                                              .withBootstrapServers(kafkaServers)
                                              .withGroupId(groupId)
                                              .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                                              
  def apply(topicName: String, limit: Int, from: Option[Long]): Source[ByteString, _] = {
    val suscription = from.map { f =>
                                  val topicPartition = new TopicPartition(topicName, 0)
                                  Subscriptions.assignmentWithOffset(topicPartition, f) }
                          .getOrElse(Subscriptions.topics(topicName))
                          
    Consumer.atMostOnceSource(consumerSettings, suscription)
            .take(limit)
            .map { record =>
              ByteString(MessagePacking.pack(Message(record.offset(), record.value())))
            }
  }
}