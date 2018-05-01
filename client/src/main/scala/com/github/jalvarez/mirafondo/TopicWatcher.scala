package com.github.jalvarez.mirafondo

import org.scalajs.dom
import dom.raw.XMLHttpRequest

object TopicWatcher {
  def run(urlPath: String): Unit = {
    val topicName = urlPath.substring(urlPath.lastIndexOf("/") + 1)
    val items = dom.document.getElementById("items")
    val xhr = HttpRequestSimplificator(new XMLHttpRequest())
    val messageList = new MessageList(xhr)
    
    messageList.setOnNewMessage { message =>
      val item = dom.document.createElement("li")
      item.textContent = message.content 
      items.appendChild(item)
    }
    .load(topicName)
  }
}