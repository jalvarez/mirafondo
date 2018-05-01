package com.github.jalvarez.mirafondo

import org.scalatest.Matchers
import org.scalatest.WordSpec
import org.scalamock.scalatest.MockFactory
import shared.MessagePackageDefinition
import org.scalajs.dom.raw.ProgressEvent
import MessageUnpacking.Message
import utils.SimpleHttpRequestStub

class MessageListSpec extends WordSpec with Matchers with MockFactory with MessagePackageDefinition {
  "A messages list" when {
    "is loaded" must {
      "send a xml http request to /topic/..." in {
        val f = fixtures
        
        val xhrMock = mock[SimpleHttpRequest]
        
        inSequence {
          (xhrMock.open _).expects("GET", s"/${f.context}/topic/${f.topic}")
          (xhrMock.setOnprogressCallback _).expects(*)
          (xhrMock.send _).expects(*)
        }
        
        val list = new MessageList(xhrMock, f.context)
        list.load(f.topic)
      }
    }
    
    "receive a new message from server" must {
      "invoke onNewMessage callback" in {
        val f = fixtures
        val packedMessage = s"${messageHead} 101\ntest\n${messageTail}"
        
        val xhrStub = new SimpleHttpRequestStub()
        xhrStub.setResponseText(packedMessage)
        
        val list = new MessageList(xhrStub, f.context)
        val onNewMessageMock = mockFunction[Message, Unit]
        list.setOnNewMessage(onNewMessageMock).load(f.topic) 
        
        onNewMessageMock.expects(*).once()
        
        xhrStub.onprogress(Unit)
      }
    }
    
    "receive two messages from server" must {
      "invoke onNewMessage callback twice" in {
        val f = fixtures
        val packedMessages = (1 to 2).map { i => s"${messageHead} ${i}\ntest${i}\n${messageTail}" }
        
        val xhrStub = new SimpleHttpRequestStub()
        val list = new MessageList(xhrStub, f.context)
        val onNewMessageMock = mockFunction[Message, Unit]
        list.setOnNewMessage(onNewMessageMock).load(f.topic) 
        
        onNewMessageMock.expects(*).twice()
        
        xhrStub.setResponseText(packedMessages(0))
        xhrStub.onprogress(Unit)
        xhrStub.setResponseText(packedMessages.mkString("\n"))
        xhrStub.onprogress(Unit)
      }
    }
  }
  
  def fixtures = new {
    val context = "test"
    val topic = "test-topic"
  }
}