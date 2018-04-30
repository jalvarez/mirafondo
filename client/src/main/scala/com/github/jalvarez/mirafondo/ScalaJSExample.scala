package com.github.jalvarez.mirafondo

import com.github.jalvarez.mirafondo.shared.SharedMessages
import org.scalajs.dom
import org.scalajs.dom.raw.XMLHttpRequest

object ScalaJSExample {

  def main(args: Array[String]): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks // TODO REMOVE
    var readResponse = 0
    val items = dom.document.getElementById("items")
    val xhr = HttpRequestSimplificator(new XMLHttpRequest())
    val messageList = new MessageList(xhr)
    
    messageList.setOnNewMessage { message =>
      val item = dom.document.createElement("li")
      item.textContent = message.content 
      items.appendChild(item)
    }
    .load("test")
  }
}
