package com.github.jalvarez.mirafondo

import shared.MessagePackageDefinition
import org.scalatest.Matchers
import org.scalatest.WordSpec
import MessageUnpacking.Message

class MessageUnpackingSpec extends WordSpec with Matchers with MessagePackageDefinition {
  "Message unpacking" when {
    "receive a packed message" must {
      val position = 42
      val messageContent = "test"
      val packedMessage = s"${messageHead} ${position}\n${messageContent}\n${messageTail}"
      
      val messages = MessageUnpacking.unpack(packedMessage)

      "return a message" in {
        messages should matchPattern { case Seq(_) => }
      }
      
      "return a message with position" in {
        messages(0).position shouldBe position
      }
      
      "return a message with content" in {
        messages(0).content shouldBe messageContent
      }
    }
    
    "receive an incomplete packed message" must {
      val incompleteMessage = s"${messageHead} 101\nincomplete test\n"
      
      val messages = MessageUnpacking.unpack(incompleteMessage)
      
      "return none" in {
        messages shouldBe Seq.empty[Message]
      }
    }
    
    "receive an interleaved packed message" must {
      val messageContent = "interleaved test"
      val interleaved = s"prefix${messageHead} 169\n${messageContent}\n${messageTail}suffix"
      
      val messages = MessageUnpacking.unpack(interleaved)
      
      "return a message with content" in {
        messages(0).content shouldBe messageContent
      }
    }
    
    "receive a string that contents N messages" must {
      val N = 10
      val packedMessages = (0 until N).map { i => s"${messageHead} ${i}\ncontent ${i}\n${messageTail}" }
                                      .mkString
                           
      val messages = MessageUnpacking.unpack(packedMessages)
                           
      "return a message sequence with positions" in {
        messages.map { _.position } shouldBe (0 until N)
      }
    }
  }
}