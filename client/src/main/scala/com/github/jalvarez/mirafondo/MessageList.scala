package com.github.jalvarez.mirafondo

import scala.scalajs.js
import MessageUnpacking.Message

class MessageList(xhr: SimpleHttpRequest, context: String) {
  private var readResponse = 0
  
  private var onNewMessageHandler: Function[Message, Unit] = _
  private var onRunOutMessagesHandler: Function[Option[Message], Unit] = _
  private var lastMessage: Option[Message] = None
  
  def load(topicName: String, from: Option[Long]): MessageList = {
    val queryParameters = from.map{ f => s"?from=${f}" }.getOrElse("")
    xhr.open("GET", s"/${context}/topic/${topicName}${queryParameters}")
    xhr.setOnprogressCallback { _: Unit =>
      if (xhr.responseText.length > readResponse) {
        val newResponseText = xhr.responseText.substring(readResponse, xhr.responseText.length)
        val messages = MessageUnpacking.unpack(newResponseText)
        messages.foreach { message =>
          onNewMessageHandler(message)
        }
        lastMessage = Some(messages.last)
        readResponse += messages.last.lastIndex
      }
    }
    xhr.setOnloadCallback { _:Unit =>
      onRunOutMessagesHandler(lastMessage)
    }
    xhr.send(js.undefined)
    this
  }
  
  def onNewMessage(sonm: Function[Message, Unit]):MessageList = { 
    onNewMessageHandler = sonm
    this
  }
  
  def onRunOutMessages(handler: Function[Option[Message], Unit]): MessageList = { 
    onRunOutMessagesHandler = handler
    this
  }  
}