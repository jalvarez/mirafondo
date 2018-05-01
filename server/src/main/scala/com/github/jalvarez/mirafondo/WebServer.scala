package com.github.jalvarez.mirafondo

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

object WebServer {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("server-system")
    implicit val materializer = ActorMaterializer()

    val config = ConfigFactory.load()
    val interface = config.getString("http.interface")
    val port = config.getInt("http.port")
    val kafkaServers = config.getString("kafka.servers")
    val groupId = config.getString("kafka.group-id")

    val service = new WebService {
      override val messageSource: MessageSource = utils.FakeMessageSource //new KafkaMessageSource(system, kafkaServers, groupId)
    }

    Http().bindAndHandle(service.route, interface, port)

    println(s"Server online at http://$interface:$port")
  }
}
