package com.github.jalvarez.mirafondo

import akka.http.scaladsl.server.Directives
import com.github.jalvarez.mirafondo.twirl.Implicits._
import akka.stream.scaladsl.Source
import akka.http.scaladsl.model._
import akka.util.ByteString
import scala.concurrent.duration._

trait WebService extends Directives {
  import shared.Defaults._
  
  val context: String
  val messageSource: MessageSource
  
  def route = {
    pathPrefix(context) {
      pathSingleSlash {
        get {
          complete {
            html.index.render(context)
          }
        }
      } ~
      path("watch" / Remaining) { topicName =>
        get {
          complete {
            html.watch.render(context, topicName)
          }
        } 
      } ~
      path("topic" / Remaining) { topicName =>
        get {
          complete(HttpEntity(ContentType(MediaTypes.`application/json`),
                   messageSource(topicName, MESSAGES_LIMIT)))
        }
      } ~
      pathPrefix("assets" / Remaining) { file =>
        // optionally compresses the response with Gzip or Deflate
        // if the client accepts compressed responses
        encodeResponse {
          getFromResource("public/" + file)
        }
      }
    }
  }
}
