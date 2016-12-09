name := "MyBlog"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8")

lazy val root = (project in file(".")).enablePlugins(SbtTwirl)

libraryDependencies ++= {
  val akkaV = "2.4.14"
    val sprayV = "1.3.3"
  val logbackV = "1.1.3"
  val akkaHttpV = "10.0.0"
  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "org.slf4j" % "slf4j-api" % "1.7.21",
    "com.typesafe.akka" % "akka-slf4j_2.11" % akkaV,
    "ch.qos.logback" % "logback-classic" % logbackV,
    "org.pegdown" % "pegdown" % "1.4.2",
    "org.mapdb" % "mapdb" % "3.0.1",
    "org.specs2" %% "specs2-core" % "2.3.11" % "test",
    "io.spray"            %%  "spray-json" % "1.3.2"
  )
}
