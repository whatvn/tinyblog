lazy val root = project.in(file(".")).enablePlugins(SbtTwirl)

name := "scalaBlog"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions   := Seq("-feature", "-deprecation", "-Xlint")

val circeVersion = "0.4.1"

//libraryDependencies += "io.circe"                   %% "circe-core"       % circeVersion
//libraryDependencies += "io.circe"                   %% "circe-generic"    % circeVersion
//libraryDependencies += "io.circe"                   %% "circe-parser"       % circeVersion
//libraryDependencies += "com.squareup.okhttp3"        % "okhttp"           % "3.2.0"
//libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging"    % "3.1.0"
//
//libraryDependencies += "org.scalatest"     %% "scalatest"       % "2.2.6"     % "test"
//libraryDependencies += "ch.qos.logback"     % "logback-classic" % "1.1.7"    % "test"
//
