package com.github.jalvarez.mirafondo

import akka.stream.scaladsl.Source
import akka.util.ByteString
import scala.concurrent.duration._
import MessagePacking.Message

object MessageSource {
  def apply(): Source[ByteString, _] = {
    Source.tick(250.milliseconds, 500.milliseconds, "test")
          .take(10)
          .zipWithIndex
          .map{ case (s, i) => 
             ByteString(MessagePacking.pack(Message(i, s)))
           }
  }
}