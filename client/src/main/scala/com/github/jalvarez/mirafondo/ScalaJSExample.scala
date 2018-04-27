package com.github.jalvarez.mirafondo

import com.github.jalvarez.mirafondo.shared.SharedMessages
import org.scalajs.dom
import org.scalajs.dom.raw.XMLHttpRequest

object ScalaJSExample {

  def main(args: Array[String]): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
    var readResponse = 0
    val items = dom.document.getElementById("items")
    val xhr = new XMLHttpRequest()
    xhr.open("GET", "/topic")
    xhr.onload = { _ =>
      dom.document.getElementById("scalajsShoutOut").textContent = xhr.responseText
    }
    xhr.onprogress = { _ =>
      if (xhr.responseText.length > readResponse) {
        val newResponseText = xhr.responseText.substring(readResponse, xhr.responseText.length)
        val messages = MessageUnpacking.unpack(newResponseText)
        messages.foreach { message =>
          val item = dom.document.createElement("li")
          item.textContent = message.content 
          items.appendChild(item)
        }
        readResponse += messages.last.lastIndex
      }
    }
    xhr.send()
  }
}
