package com.github.jalvarez.mirafondo

import com.github.jalvarez.mirafondo.shared.SharedMessages
import org.scalajs.dom
import org.scalajs.dom.raw.XMLHttpRequest

object ScalaJSExample {

  def main(args: Array[String]): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
    val xhr = new XMLHttpRequest()
    xhr.open("GET", "/topic")
    xhr.onload = { _ =>
      dom.document.getElementById("scalajsShoutOut").textContent = xhr.responseText
    }
    xhr.send()
  }
}
