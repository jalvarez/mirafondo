package com.github.jalvarez.mirafondo.utils

import akka.stream.scaladsl.Source
import akka.util.ByteString
import scala.concurrent.duration._
import com.github.jalvarez.mirafondo.MessagePacking
import MessagePacking.Message
import com.github.jalvarez.mirafondo.MessageSource

object FakeMessageSource extends MessageSource {
  def apply(topicName: String, limit: Int, from: Option[Long]): Source[ByteString, _] = {
    Source.tick(250.milliseconds, 500.milliseconds, topicName)
          .take(limit)
          .zipWithIndex
          .map{ case (_, i) => 
             ByteString(MessagePacking.pack(Message(i + from.getOrElse(0L), fakeMessage)))
           }
  }
  
  private val fakeMessage = {
    """{
        "users": [
          {
            "id": 0,
            "name": "Adam Carter",
            "work": "acme",
            "email": "adam.carter@acme.com",
            "dob": "1978",
            "address": "83 Warner Street",
            "city": "Boston",
            "optedin": true
          },
          {
            "id": 1,
            "name": "Leanne Brier",
            "work": "Connic",
            "email": "leanne.brier@tia.org",
            "dob": "13/05/1987",
            "address": "9 Coleman Avenue",
            "city": "Toronto",
            "optedin": false
          }
        ],
        "images": [
          "img0.png",
          "img1.png",
          "img2.png"
        ],
        "coordinates": {
        	"x": 35.12,
        	"y": -21.49
        },
        "price": "$59,395"
      }"""
  }
}