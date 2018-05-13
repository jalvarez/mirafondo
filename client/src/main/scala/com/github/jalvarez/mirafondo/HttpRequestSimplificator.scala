package com.github.jalvarez.mirafondo

import scala.scalajs.js
import org.scalajs.dom.raw.XMLHttpRequest

case class HttpRequestSimplificator(xhr: XMLHttpRequest) extends SimpleHttpRequest {
  def open(method: String, url: String): Unit = { xhr.open(method, url) }

  def send(data: js.Any): Unit = { xhr.send(data) }

  def responseText: String = { xhr.responseText }

  def setOnprogressCallback(c: Function1[Unit, _]) = {
    xhr.onprogress = { _ => c(Unit) }
  }
  
  def setOnloadCallback(c: Function1[Unit, _]) = {
    xhr.onload = { _ => c(Unit) }
  }
}