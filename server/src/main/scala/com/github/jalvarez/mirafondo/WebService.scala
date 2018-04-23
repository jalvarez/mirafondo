package com.github.jalvarez.mirafondo

import akka.http.scaladsl.server.Directives
import com.github.jalvarez.mirafondo.shared.SharedMessages
import com.github.jalvarez.mirafondo.twirl.Implicits._
import akka.stream.scaladsl.Source
import akka.http.scaladsl.model._
import akka.util.ByteString
import scala.concurrent.duration._

class WebService() extends Directives {

  val route = {
    pathSingleSlash {
      get {
        complete {
          com.github.jalvarez.mirafondo.html.index.render(SharedMessages.itWorks)
        }
      }
    } ~
    path("topic") {
      get {
        complete(HttpEntity(ContentType(MediaTypes.`application/json`), 
                 Source.tick(250.milliseconds, 500.milliseconds, "test").take(10).map { s => ByteString(s)}))
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
