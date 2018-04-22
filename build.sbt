lazy val akkaHttpVersion = "10.1.1"
lazy val akkaVersion    = "2.5.11"

lazy val server = (project in file("server")).settings(commonSettings).settings(
  name := "mirafondo_server",
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.vmunier" %% "scalajs-scripts" % "1.1.2",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "com.typesafe.akka" %% "akka-stream-kafka" % "0.20",

    "com.typesafe.akka"	%% "akka-http-testkit" % akkaHttpVersion % Test,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
    "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    "org.scalamock"	%% "scalamock" % "4.0.0" % Test,
    "net.cakesolutions"	%% "scala-kafka-client-akka" % "0.10.2.2" % Test,
    "net.cakesolutions"	%% "scala-kafka-client-testkit"	% "0.10.2.2" % Test
  ),
  WebKeys.packagePrefix in Assets := "public/",
  managedClasspath in Runtime += (packageBin in Assets).value,
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for Twirl templates are present
  EclipseKeys.preTasks := Seq(compile in Compile)
).enablePlugins(SbtWeb, SbtTwirl).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(commonSettings).settings(
  name := "mirafondo_client",
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.5",
    "org.scalatest" %%% "scalatest" % "3.0.5" % "test"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).settings(
  name := "mirafondo_shared",
  commonSettings
)
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val commonSettings = Seq(
  scalaVersion := "2.12.3",
  organization := "com.github.jalvarez"
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
