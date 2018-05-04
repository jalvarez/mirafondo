package com.github.jalvarez.mirafondo

import org.scalatest.WordSpec
import org.scalatest.Matchers

class TopicWatchUrlSpec extends WordSpec with Matchers {
  "A topic watch url" must {
    "return the topic name" when {
      val topicName = "topic_name"
      
      Seq(s"/context/watch/${topicName}", s"/context/watch/${topicName}/",
          s"/context/watch/${topicName}?from=42", s"/context/watch/${topicName}/?from=42").foreach { url => 
        s"the url is ${url}" in {
          TopicWatchUrl(url).topicName shouldBe topicName
        }
      }
    }
    
    "return the position" when {
      val position = 101
      
      Seq(s"/context/watch/topic_name?from=${position}",
          s"/context/watch/topic_name/?from=${position}").foreach { url => 
            s"the url is ${url}" in {
              TopicWatchUrl(url).position shouldBe Some(position)
            }
      }
    }
  }
}