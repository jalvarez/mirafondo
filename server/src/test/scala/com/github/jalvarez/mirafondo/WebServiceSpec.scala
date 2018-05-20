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
    val webservice = new WebService {
        override val context = "test"
        override val messageSource = mock[MessageSource]
    }
    
    "call to message source for get topic with from parameter" in {
      val from = 101L
      
      (webservice.messageSource.apply _).expects(*, *, Some(from)).returning { Source.empty[ByteString] }
      
      Get(s"/${webservice.context}/topic/test?from=${from}") ~> webservice.route ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
    
    "call to message source for get topic with limit parameter" in {
      val limit = 50
      
      (webservice.messageSource.apply _).expects(*, limit, *).returning { Source.empty[ByteString] }
      
      Get(s"/${webservice.context}/topic/test?limit=${limit}") ~> webservice.route ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
    
    "return welcome page without end slash" in {
      Get(s"/${webservice.context}") ~> webservice.route ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
    
    "redirect to welcome page with end slash" in {
      Get(s"/${webservice.context}/") ~> webservice.route ~> check {
        status shouldEqual StatusCodes.MovedPermanently
      }
    }
  }
}