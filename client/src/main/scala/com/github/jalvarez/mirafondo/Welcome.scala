package com.github.jalvarez.mirafondo

import org.scalajs.dom
import scala.util.Try

object Welcome {
  def run(context: String, urlPath: String): Unit = {
    for (input <- Try(dom.document.getElementById("inputTopic").asInstanceOf[dom.html.Input]);
         button <- Try(dom.document.getElementById("watchButton").asInstanceOf[dom.html.Button])) {
      input.onkeydown = { ke =>
        if (ke.keyCode == 13) {
          if (input.value.length > 0) {
            dom.window.location.pathname = s"/${context}/watch/${input.value}"
          }
        }
      }
      
      button.onclick = { _ =>
        if (input.value.length > 0) {
          dom.window.location.pathname = s"/${context}/watch/${input.value}"
        }
      }
    }
  }
}
