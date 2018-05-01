package com.github.jalvarez.mirafondo

import org.scalajs.dom
import dom.raw.XMLHttpRequest

object TopicWatcher {
  def run(context: String, urlPath: String): Unit = {
    val topicName = urlPath.substring(urlPath.lastIndexOf("/") + 1)
    val xhr = HttpRequestSimplificator(new XMLHttpRequest())
    val messageList = new MessageList(xhr, context)
    val messageRender = new MessageRender(dom.document.getElementById("items"))
    
    messageList.setOnNewMessage { message => messageRender.add(message) }.load(topicName)
  }
}