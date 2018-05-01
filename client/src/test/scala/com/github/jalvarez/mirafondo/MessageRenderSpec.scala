package com.github.jalvarez.mirafondo

import org.scalatest.Matchers
import org.scalatest.WordSpec
import org.scalajs.dom
import MessageUnpacking.Message

class MessageRenderSpec extends WordSpec with Matchers {
  "The message render" when {
    "a message is added" must {
      "add a list item" in {
        val f = fixtures
        
        val render = new MessageRender(f.list)
        render.add(f.message)
        
        f.list.childElementCount should be > 0
      }
      
      "include the message's content" in {
        val f = fixtures
        
        val render = new MessageRender(f.list)
        render.add(f.message)
        
        f.list.innerHTML should include (f.message.content)
      }
    }
  }
  
  def fixtures = new {
    val list = dom.document.createElement("ul")
    val message = Message(101, "test", 42)
  }
}