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
    var firstPositionInList: Option[Long] = None
    
    messageList.onNewMessage { message =>
                  messageRender.add(message)
                  if (firstPositionInList.isEmpty) {
                    firstPositionInList = Some(message.position)
                    val previousPosition = for (pos <- firstPositionInList;
                                                newPos <- Some(pos - Defaults.MESSAGES_LIMIT) if newPos > 0)
                                                  yield newPos
                    updateButton("previous-button", topicWatchPath, previousPosition)
                    
                    val firstPosition = for (pos <- firstPositionInList;
                                             newPos = 0L if pos > 0)
                                               yield newPos
                    updateButton("first-button", topicWatchPath, firstPosition)
                  }
                }
               .onRunOutMessages { lastMessage =>
                 updateButton("next-button", topicWatchPath, lastMessage.map{_.position + 1})
               }
               .load(topicWatchPath.topicName, topicWatchPath.position)
               
    for (button <- Try(dom.document.getElementById("latest-button").asInstanceOf[dom.html.Button])) {
      button.style.visibility = "visible"
      button.onclick = { _ =>
        dom.window.location.href = topicWatchPath.newPathToLatest
      }
    }
  }
  
  private def updateButton(buttonName: String,
                           topicWatchPath: TopicWatchPath,
                           previousPosition: Option[Long]): Unit = {
    for (button <- Try(dom.document.getElementById(buttonName).asInstanceOf[dom.html.Button])) {
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