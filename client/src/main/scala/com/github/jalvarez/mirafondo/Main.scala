package com.github.jalvarez.mirafondo

import org.scalajs.dom

object Main {
  def main(args: Array[String]): Unit = {
    val pathname = dom.window.location.pathname
    val path = "^/([^/]+)/([^/]+)?.*".r
    
    pathname match {
      case path(context, "watch") =>
        TopicWatcher.run(context, pathname)
      case path(context, _) =>
        Welcome.run(context, pathname)
      case _ =>
        // Nothing
    }
  }
}