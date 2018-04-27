package com.github.jalvarez.mirafondo

import shared.MessagePackageDefinition

object MessagePacking extends MessagePackageDefinition {
  
  def pack(message: Message): String = {
    s"${messageHead} ${message.position}\n${message.content}\n${messageTail}\n"
  }
  
  case class Message(position: Long, content: String)
}