package com.github.jalvarez.mirafondo

import shared.MessagePackageDefinition
import scala.collection.mutable.Buffer

object MessageUnpacking extends MessagePackageDefinition {
  private val headOfMessage = s"$messageHead ([0-9]+)\n".r
  private val endOfMessage = s"\n$messageTail".r
  
  def unpack(packedMessage: String): Seq[Message] = {
    val messages = Buffer.empty[Message]
    val heads = headOfMessage.findAllIn(packedMessage)
    
    while (heads.hasNext) {
      heads.next()
      val contentBegin = heads.end
      val contentEndMatch = endOfMessage.findFirstMatchIn(packedMessage.substring(contentBegin))
      if (contentEndMatch.isDefined) {
        val position = heads.group(1).toInt
        val contentEnd = contentBegin + contentEndMatch.get.start
        val lastIndex = contentBegin + contentEndMatch.get.end
        messages += Message(position, packedMessage.substring(contentBegin, contentEnd), lastIndex)
      }
    }
    messages.toSeq
  }
  
  case class Message(position: Int, content: String, lastIndex: Int)
}