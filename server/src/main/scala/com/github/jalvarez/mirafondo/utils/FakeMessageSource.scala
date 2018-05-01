package com.github.jalvarez.mirafondo.utils

import akka.stream.scaladsl.Source
import akka.util.ByteString
import scala.concurrent.duration._
import com.github.jalvarez.mirafondo.MessagePacking
import MessagePacking.Message
import com.github.jalvarez.mirafondo.MessageSource

object FakeMessageSource extends MessageSource {
  def apply(topicName: String, limit: Int): Source[ByteString, _] = {
    Source.tick(250.milliseconds, 500.milliseconds, topicName)
          .take(limit)
          .zipWithIndex
          .map{ case (s, i) => 
             ByteString(MessagePacking.pack(Message(i, s)))
           }
  }
}