name := "MyBlog"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val root = (project in file(".")).enablePlugins(SbtTwirl)

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-caching" % sprayV,
    "io.spray"            %%  "spray-client" % sprayV,
    "io.spray"            %%  "spray-json" % "1.3.2",
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "org.mapdb"           %  "mapdb"          % "3.0.1",
    "org.pegdown"         % "pegdown"         % "1.6.0",
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test"
  )
}
