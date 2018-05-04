package com.github.jalvarez.mirafondo

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.Matchers
import org.scalatest.WordSpec
import org.scalamock.scalatest.MockFactory
import akka.http.scaladsl.model.StatusCodes
import akka.stream.scaladsl.Source
import akka.util.ByteString

class WebServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with MockFactory {
  "The Webservice" should {
    "call to message source for get topic with from parameter" in {
      val webservice = new WebService {
          override val context = "test"
          override val messageSource = mock[MessageSource]
      }
      val from = 101L
      
      (webservice.messageSource.apply _).expects(*, *, Some(from)).returning { Source.empty[ByteString] }
      
      Get(s"/${webservice.context}/topic/test?from=${from}") ~> webservice.route ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
  }
}