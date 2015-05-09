name := """ripper"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.6" % "test",
  "org.specs2" %% "specs2-mock" % "3.6" % "test",
  "org.scaldi" %% "scaldi" % "0.5.5",
  "joda-time" % "joda-time" % "2.7",
  "org.scalafx" %% "scalafx" % "2.2.76-R11",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

unmanagedJars in Compile +=
  Attributed.blank(
    file(scala.util.Properties.javaHome) / "lib" / "jfxrt.jar")

mainClass in run in Compile := Some("gui.GuiMain")