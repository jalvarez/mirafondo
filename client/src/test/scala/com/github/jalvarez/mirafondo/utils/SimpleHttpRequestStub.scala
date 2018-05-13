package com.github.jalvarez.mirafondo.utils

import scala.scalajs.js
import com.github.jalvarez.mirafondo.SimpleHttpRequest

class SimpleHttpRequestStub extends SimpleHttpRequest {
  var _responseText: String = _
  var onprogress: Function1[Unit, _] = _
  var onload: Function1[Unit, _] = _
  
  def open(method: String, url: String): Unit = {
  }
  
  def send(data: js.Any): Unit = {
  }
  
  def setOnprogressCallback(c: Function1[Unit, _]) = {
    onprogress = c
  }
  
  def responseText: String = _responseText
  
  def setResponseText(rt: String) = {
    _responseText = rt
  }

  def setOnloadCallback(c: Function1[Unit, _]): Unit = {
    onload = c
  }
}