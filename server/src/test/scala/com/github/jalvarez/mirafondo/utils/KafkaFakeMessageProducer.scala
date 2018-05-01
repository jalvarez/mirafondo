package com.github.jalvarez.mirafondo.utils

import com.github.jalvarez.mirafondo.shared.Defaults
import scala.concurrent.duration._
import akka.stream.scaladsl.Source
import akka.kafka.scaladsl.Producer
import org.apache.kafka.common.serialization.StringSerializer
import akka.kafka.ProducerSettings
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.UUID
import akka.stream.ActorMaterializer
import scala.concurrent.Await
import org.slf4j.LoggerFactory

object KafkaFakeMessageProducer extends App {
  private lazy val logger = LoggerFactory.getLogger("com.github.jalvarez.mirafondo.utils.KafkaFakeMessageProducer")
  import Defaults._
  
  if (args.length < 1) {
    println("Error: you must include topic name as parameter")
    System.exit(-1)
  }

  val topicName = args(0)
  val limit = if (args.length < 2) MESSAGES_LIMIT else args(1).toInt
  
  val config = ConfigFactory.load()
  val kafkaServers = config.getString("kafka.servers")
  
  implicit val system = ActorSystem("KafkaFakeMessageProducer", config)
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher
  
  lazy val producerSettings = ProducerSettings(system, new StringSerializer, new StringSerializer)
                                              .withBootstrapServers(kafkaServers)

  Await.ready(Source.tick(250.milliseconds, 500.milliseconds, topicName)
                    .take(limit)
                    .zipWithIndex
                    .map{ case (s, i) =>
                      val message = s"${s}${i}"
                      if (logger.isDebugEnabled)
                        logger.debug(s"Message sent: ${message}")
                      new ProducerRecord(topicName, UUID.randomUUID().toString, message)
                    }
                    .runWith(Producer.plainSink(producerSettings))
                    .andThen {
                      case _ =>
                        system.terminate()
                    },
                    Duration.Inf)
}