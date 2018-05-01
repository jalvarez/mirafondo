package com.github.jalvarez.mirafondo

import org.scalajs.dom
import MessageUnpacking.Message

class MessageRender(elementList: dom.Element) {
  def add(message: Message): Unit = {
    val li = dom.document.createElement("li")
    li.textContent = message.content
    elementList.appendChild(li)
  }
}