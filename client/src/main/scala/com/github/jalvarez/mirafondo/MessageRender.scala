package com.github.jalvarez.mirafondo

import org.scalajs.dom
import scalatags.JsDom.all._
import MessageUnpacking.Message

class MessageRender(elementList: dom.Element) {
  def add(message: Message): Unit = {
    val item = li(`class` := "list-group-item d-flex justify-content-between align-items-center")(
                 pre()(message.content),
                 span(`class`:= "badge badge-primary badge-pill")(message.position.toString)
               ).render
               
    if (elementList.childElementCount > 0)
      elementList.insertBefore(item, elementList.firstChild)
    else
      elementList.appendChild(item)
  }
}