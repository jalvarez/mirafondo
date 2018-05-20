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
          (xhrMock.setOnloadCallback _).expects(*)
          (xhrMock.send _).expects(*)
        }
        
        val list = new MessageList(xhrMock, f.context)
        list.load(f.topic, None)
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
        list.onNewMessage(onNewMessageMock).load(f.topic, None) 
        
        onNewMessageMock.expects(*).once()
        
        xhrStub.onprogress(Unit)
      }
    }
    
    "receive only a message from server" must {      
      "invoke onRunOutMessages callback" in {
        val f = fixtures
        val packedMessage = s"${messageHead} 101\ntest\n${messageTail}"
        
        val xhrStub = new SimpleHttpRequestStub()
        xhrStub.setResponseText(packedMessage)
        
        val list = new MessageList(xhrStub, f.context)
        val onRunOutMessages = mockFunction[Option[Message], Unit]
        list.onRunOutMessages(onRunOutMessages).load(f.topic, None) 
        
        onRunOutMessages.expects(*).once()
        
        xhrStub.onload(Unit)
      }
    }
    
    "receive two messages from server" must {
      "invoke onNewMessage callback twice" in {
        val f = fixtures
        val packedMessages = (1 to 2).map { i => s"${messageHead} ${i}\ntest${i}\n${messageTail}" }
        
        val xhrStub = new SimpleHttpRequestStub()
        val list = new MessageList(xhrStub, f.context)
        val onNewMessageMock = mockFunction[Message, Unit]
        list.onNewMessage(onNewMessageMock).load(f.topic, None) 
        
        onNewMessageMock.expects(*).twice()
        
        xhrStub.setResponseText(packedMessages(0))
        xhrStub.onprogress(Unit)
        xhrStub.setResponseText(packedMessages.mkString("\n"))
        xhrStub.onprogress(Unit)
      }
    }
    
    "is loaded from a specific position" must {
      "send a xml http request using a from parameter" in {
        val f = fixtures
        val from = 101L
        
        val xhrMock = mock[SimpleHttpRequest]
        
        inSequence {
          (xhrMock.open _).expects("GET", s"/${f.context}/topic/${f.topic}?from=${from}")
          (xhrMock.setOnprogressCallback _).expects(*)
          (xhrMock.setOnloadCallback _).expects(*)
          (xhrMock.send _).expects(*)
        }
        
        val list = new MessageList(xhrMock, f.context)
        list.load(f.topic, Some(from))
      }
    }
    
    "is loaded with a limit not equal to MESSAGES_LIMIT" must {
      "send a xml http request using a limit parameter" in {
        val f = fixtures
        val limit = 50
        
        val xhrMock = mock[SimpleHttpRequest]
        
        inSequence {
          (xhrMock.open _).expects("GET", s"/${f.context}/topic/${f.topic}?limit=${limit}")
          (xhrMock.setOnprogressCallback _).expects(*)
          (xhrMock.setOnloadCallback _).expects(*)
          (xhrMock.send _).expects(*)
        }
        
        val list = new MessageList(xhrMock, f.context)
        list.load(f.topic, None, limit)
      }
    }
  }
  
  def fixtures = new {
    val context = "test"
    val topic = "test-topic"
  }
}