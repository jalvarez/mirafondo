package com.github.jalvarez.mirafondo

import org.scalatest.Matchers
import org.scalatest.WordSpec
import org.scalajs.dom
import MessageUnpacking.Message

class MessageRenderSpec extends WordSpec with Matchers {
  "The message render" when {
    "a message is added" must {
      val f = fixtures
      
      val render = new MessageRender(f.list)
      render.add(f.message)
      
      "add a list item" in {
        f.list.childElementCount should be > 0
      }
      
      "include the message's content" in {
        f.list.innerHTML should include (f.message.content)
      }
      
      "include the message's position" in {
        f.list.innerHTML should include (f.message.position.toString)
      }
    }
    
    "two messages are added" must {
      val f = fixtures
      val secondMessage = Message(102, "test-2", 84)
      val render = new MessageRender(f.list)
      render.add(f.message)
      render.add(secondMessage)
      
      "include the last message as first" in {
        f.list.children(0).innerHTML should include (secondMessage.position.toString)
      }
    }
  }
  
  def fixtures = new {
    val list = dom.document.createElement("ul")
    val message = Message(101, "test", 42)
  }
}