package com.github.jalvarez.mirafondo

import org.scalatest.WordSpec
import org.scalatest.Matchers

class TopicWatchPathSpec extends WordSpec with Matchers {
  "A topic watch path" must {
    "return the topic name" when {
      val topicName = "topic_name"
      
      Seq(s"/context/watch/${topicName}", s"/context/watch/${topicName}/",
          s"/context/watch/${topicName}?from=42", s"/context/watch/${topicName}/?from=42").foreach { path => 
        s"the path is ${path}" in {
          TopicWatchPath(path).topicName shouldBe topicName
        }
      }
    }
    
    "return the position" when {
      val position = 101
      
      Seq(s"/context/watch/topic_name?from=${position}",
          s"/context/watch/topic_name/?from=${position}").foreach { path => 
            s"the path is ${path}" in {
              TopicWatchPath(path).position shouldBe Some(position)
            }
      }
    }
  }
}