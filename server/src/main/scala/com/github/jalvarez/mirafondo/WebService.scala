package com.github.jalvarez.mirafondo

import akka.http.scaladsl.server.Directives
import com.github.jalvarez.mirafondo.twirl.Implicits._
import akka.stream.scaladsl.Source
import akka.http.scaladsl.model._
import akka.util.ByteString
import scala.concurrent.duration._

trait WebService extends Directives {
  import shared.Defaults._
  
  val messageSource: MessageSource
  
  val route = {
    pathSingleSlash {
      get {
        complete {
          html.index.render("")
        }
      }
    } ~
    path("watch" / Remaining) { topicName =>
      get {
        complete {
          html.watch.render(topicName)
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
