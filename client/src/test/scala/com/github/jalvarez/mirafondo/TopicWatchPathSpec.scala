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
    
    "return a new path with position" when {
      val previousPosition = 42
      Seq("/context/watch/topic_name",
          s"/context/watch/topic_name?from=${previousPosition}",
          s"/context/watch/topic_name/?from=${previousPosition}").foreach { path => 
            s"update the position and the path is ${path}" in {
              val position = 142
              val newPath = TopicWatchPath(path).newPathTo(position) 
              
              TopicWatchPath(newPath).position shouldBe Some(position)
            }
      }
    }
    
    "return a new path without position" when {
      val previousPosition = 42
      Seq(s"/context/watch/topic_name?from=${previousPosition}",
          s"/context/watch/topic_name/?from=${previousPosition}").foreach { path => 
            s"update the position and the path is ${path}" in {
              val newPath = TopicWatchPath(path).newPathToLatest
              
              TopicWatchPath(newPath).position shouldBe None
            }
      }
    }
  }
}