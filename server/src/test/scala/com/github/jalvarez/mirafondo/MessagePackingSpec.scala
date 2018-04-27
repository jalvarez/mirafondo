package com.github.jalvarez.mirafondo

import org.scalatest.WordSpec
import org.scalatest.Matchers
import shared.MessagePackageDefinition
import MessagePacking.Message

class MessagePackingSpec extends WordSpec with Matchers with MessagePackageDefinition {
  "Message packing" when {
    val message = Message(101, "test")
    
    "packed a message" must {
      val packedMessage = MessagePacking.pack(message)
      
      s"start with ${messageHead}" in {
        packedMessage should startWith(messageHead)
      }
      
      "has the position in first line" in {
        packedMessage should startWith regex ".+ [0-9]+\n".r
      }
     
      "include the content" in {
        packedMessage should include (message.content)
      }
      
      s"has ${messageTail} in last line" in {
        packedMessage should endWith(messageTail + "\n")
      }
    }
  }
  
  val fixtures = new {
    val message = Message(101, "test")
  }
}