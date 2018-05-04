package com.github.jalvarez.mirafondo

import scala.scalajs.js
import MessageUnpacking.Message

class MessageList(xhr: SimpleHttpRequest, context: String) {
  private var readResponse = 0
  
  private var onNewMessage: Function[Message, Unit] = _
  
  def setOnNewMessage(sonm: Function[Message, Unit]):MessageList = { 
    onNewMessage = sonm
    this
  }
  
  def load(topicName: String, from: Option[Long]): MessageList = {
    val queryParameters = from.map{ f => s"?from=${f}" }.getOrElse("")
    xhr.open("GET", s"/${context}/topic/${topicName}${queryParameters}")
    xhr.setOnprogressCallback { _: Unit =>
      if (xhr.responseText.length > readResponse) {
        val newResponseText = xhr.responseText.substring(readResponse, xhr.responseText.length)
        val messages = MessageUnpacking.unpack(newResponseText)
        messages.foreach { message =>
          onNewMessage(message)
        }
        readResponse += messages.last.lastIndex
      }
    }
    xhr.send(js.undefined)
    this
  }
}