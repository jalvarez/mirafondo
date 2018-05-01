package com.github.jalvarez.mirafondo

import org.scalajs.dom

object Main {
  def main(args: Array[String]): Unit = {
    val pathname = dom.window.location.pathname
    val path = "^/([^/]+).*".r
    
    pathname match {
      case path("watch") =>
        TopicWatcher.run(pathname)
      case _ =>
        Welcome.run(pathname)
    }
  }
}