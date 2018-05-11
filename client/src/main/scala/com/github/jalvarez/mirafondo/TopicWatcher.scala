package com.github.jalvarez.mirafondo

import org.scalajs.dom
import dom.raw.XMLHttpRequest
import scala.util.Try
import shared.Defaults

object TopicWatcher {
  def run(context: String, urlPath: String): Unit = {
    val topicWatchPath = TopicWatchPath(urlPath + dom.window.location.search)
    val xhr = HttpRequestSimplificator(new XMLHttpRequest())
    val messageList = new MessageList(xhr, context)
    val messageRender = new MessageRender(dom.document.getElementById("items"))
    var lastPosition: Option[Long] = None
    
    messageList.setOnNewMessage { message =>
                  messageRender.add(message)
                  if (lastPosition.isEmpty) {
                    lastPosition = Some(message.position)
                    val previousPosition = for (pos <- lastPosition;
                                                newPos <- Some(pos - Defaults.MESSAGES_LIMIT) if newPos > 0)
                                                  yield newPos
                    updateButton(topicWatchPath, previousPosition)
                  }
                }
               .load(topicWatchPath.topicName, topicWatchPath.position)
  }
  
  private def updateButton(topicWatchPath: TopicWatchPath, previousPosition: Option[Long]): Unit = {
    for (button <- Try(dom.document.getElementById("previous-button").asInstanceOf[dom.html.Button])) {
      previousPosition match {
        case Some(pos) =>
          button.style.visibility = "visible"
          button.onclick = { _ =>
            dom.window.location.href = topicWatchPath.newPathTo(pos)
          }
        case None =>
          button.style.visibility = "hidden"
      }
    }
  }
}