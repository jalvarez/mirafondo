package com.github.jalvarez.mirafondo

import scala.scalajs.js
import org.scalajs.dom.raw.ProgressEvent

trait SimpleHttpRequest {
  def open(method: String, url: String): Unit
  def send(data: js.Any): Unit
  def setOnprogressCallback(c: Function1[Unit, _])
  def setOnloadCallback(c: Function1[Unit, _])
  def responseText: String
}