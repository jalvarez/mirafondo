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
        val contentEnd = contentBegin + contentEndMatch.get.start
        messages += Message(heads.group(1).toInt, packedMessage.substring(contentBegin, contentEnd))
      }
    }
    messages.toSeq
  }
  
  case class Message(position: Int, content: String)
}