package com.github.jalvarez.mirafondo.utils

import scala.scalajs.js
import com.github.jalvarez.mirafondo.SimpleHttpRequest

class SimpleHttpRequestStub extends SimpleHttpRequest {
  var _responseText: String = _
  
  def open(method: String, url: String): Unit = {
  }
  
  def send(data: js.Any): Unit = {
  }
  
  def setOnprogressCallback(c: Function1[Unit, _]) = {
    onprogress = c
  }
  
  var onprogress: Function1[Unit, _] = _
  
  def responseText: String = _responseText
  
  def setResponseText(rt: String) = {
    _responseText = rt
  }
}