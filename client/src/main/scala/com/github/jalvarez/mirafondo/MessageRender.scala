package com.github.jalvarez.mirafondo

import org.scalajs.dom
import MessageUnpacking.Message

class MessageRender(elementList: dom.Element) {
  def add(message: Message): Unit = {
    val li = dom.document.createElement("li")
    li.textContent = s"(${message.position}) ${message.content}"
    if (elementList.childElementCount > 0)
      elementList.insertBefore(li, elementList.firstChild)
    else
      elementList.appendChild(li)
  }
}