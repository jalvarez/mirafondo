package com.github.jalvarez.mirafondo

case class TopicWatchUrl(sourceUrl: String) {
  private val url = "/([^/]+)/watch/([^/\\?]+)/?(\\?from=[0-9]+)?".r
  
  val (topicName, position) = sourceUrl match {
                                          case url(_, topic, null) => 
                                            (topic, None)
                                          case url(_, topic, fromParam) => 
                                            (topic, Some(fromParam.substring(6).toLong))
                                          case _ =>
                                            ("", None)
                                        }
}