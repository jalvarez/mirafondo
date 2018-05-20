package com.github.jalvarez.mirafondo

import scala.scalajs.js
import MessageUnpacking.Message
import shared.Defaults._

class MessageList(xhr: SimpleHttpRequest, context: String) {
  private var readResponse = 0
  
  private var onNewMessageHandler: Function[Message, Unit] = _
  private var onRunOutMessagesHandler: Function[Option[Message], Unit] = _
  private var lastMessage: Option[Message] = None
  
  def load(topicName: String, from: Option[Long], limit: Int = MESSAGES_LIMIT): MessageList = {
    val limitParam = if (limit == MESSAGES_LIMIT) None else Some(limit.toString)
    val params = Map("from" -> from.map{_.toString}, 
                    "limit" -> limitParam)
    xhr.open("GET", s"/${context}/topic/${topicName}${queryParameters(params)}")
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
  
  private def queryParameters(params: Map[String, Option[String]]): String = {
    val paramsDefined = params.filter{ case (_,v) => v.isDefined}
    if (!paramsDefined.isEmpty)
      "?" + paramsDefined.map { case (k,v) => s"$k=${v.get}" }.mkString("&")
    else
      ""
  }
}