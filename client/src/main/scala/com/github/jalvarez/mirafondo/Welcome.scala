package com.github.jalvarez.mirafondo

import org.scalajs.dom
import scala.util.Try
import shared.Defaults._

object Welcome {
  def run(context: String, urlPath: String): Unit = {
    for (inputTopic <- getInputValue("inputTopic");
         inputFrom <- getInputValue("inputFrom");
         button <- Try(dom.document.getElementById("watchButton").asInstanceOf[dom.html.Button]);
         inputLimit <- getInputValue("inputLimit");
         form <- Try(dom.document.getElementById("formWatch").asInstanceOf[dom.html.Form])) {
      
      Seq(inputTopic, inputFrom).foreach { input =>
        input.onkeydown = { ke =>
          if (ke.keyCode == 13) {
            for (tn <- topicName) navigateTo(context, tn, from)
          }
        }
      }
      
      button.onclick = { _ =>
        for (tn <- topicName) navigateTo(context, tn, from)
      }
      
      inputLimit.value = getLimit.toString
      inputLimit.onkeyup = { _ =>
        updateLimit(inputLimit.value.toInt)
      }
      
      form.onsubmit = { _ =>
        for (tn <- topicName) navigateTo(context, tn, from)
      }
    }
  }
  
  private def navigateTo(context: String, topic: String, from: Option[Long]): Unit = {
    val qs = from.map { f => s"?from=${f}" }.getOrElse("")
    dom.window.location.href = s"/${context}/watch/${topic}${qs}"
  }
  
  private def from: Option[Long] = {
    (for (input <- getInputValue("inputFrom"))
      yield { input.value.toLong }).toOption
  }
  
  private def topicName: Option[String] = {
    (for (input <- getInputValue("inputTopic"))
      yield { 
        if (input.value.length > 0) Some(input.value) else None
      }).toOption.flatten
  }
  
  private def getInputValue(inputId: String): Try[dom.html.Input] = {
    Try(dom.document.getElementById(inputId).asInstanceOf[dom.html.Input])
  }
  
  private def getLimit: Int = {
    val limitInSession = dom.window.sessionStorage.getItem("LIMIT")
    if (limitInSession != null) limitInSession.toInt else MESSAGES_LIMIT
  }
  
  private def updateLimit(limit: Int): Unit = {
    dom.window.sessionStorage.setItem("LIMIT", limit.toString)
  }
}
