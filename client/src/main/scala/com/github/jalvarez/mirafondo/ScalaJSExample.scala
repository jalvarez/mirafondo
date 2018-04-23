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
        val item = dom.document.createElement("li")
        item.textContent = xhr.responseText.substring(readResponse, xhr.responseText.length) 
        items.appendChild(item)
        readResponse = xhr.responseText.length 
      }
    }
    xhr.send()
  }
}
