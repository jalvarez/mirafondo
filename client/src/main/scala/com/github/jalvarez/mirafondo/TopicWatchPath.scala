package com.github.jalvarez.mirafondo

case class TopicWatchPath(sourcePath: String) {
  private val path = "/([^/]+)/watch/([^/\\?]+)/?(\\?from=[0-9]+)?".r
  
  val (topicName, position) = sourcePath match {
                                          case path(_, topic, null) => 
                                            (topic, None)
                                          case path(_, topic, fromParam) => 
                                            (topic, Some(fromParam.substring(6).toLong))
                                          case _ =>
                                            ("", None)
                                        }
}