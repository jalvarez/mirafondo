package com.github.jalvarez.mirafondo

case class TopicWatchPath(sourceUrl: String) {
  private val url = "/([^/]+)/watch/([^/\\?]+)/?(\\?from=[0-9]+)?".r
  
  val (context, topicName, position) = sourceUrl match {
                                                   case url(ctx, topic, null) => 
                                                      (ctx, topic, None)
                                                   case url(ctx, topic, fromParam) => 
                                                      (ctx, topic, Some(fromParam.substring(6).toLong))
                                                   case _ =>
                                                      ("", "", None)
                                                 }
  
  def newPathTo(newPosition: Long): String = s"/${context}/watch/${topicName}/?from=${newPosition}"
}