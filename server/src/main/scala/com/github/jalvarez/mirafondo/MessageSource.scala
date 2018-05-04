package com.github.jalvarez.mirafondo

import akka.stream.scaladsl.Source
import akka.util.ByteString

trait MessageSource {
  def apply(topicName: String, limit: Int, from: Option[Long]): Source[ByteString, _]
}