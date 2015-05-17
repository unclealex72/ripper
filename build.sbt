name := """ripper"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.6" % "test",
  "org.specs2" %% "specs2-mock" % "3.6" % "test",
  "joda-time" % "joda-time" % "2.7",
  "org.scalafx" %% "scalafx" % "8.0.40-R8",
  "org.controlsfx" % "controlsfx" % "8.20.8",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.4",
  "io.argonaut" %% "argonaut" % "6.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar"))

mainClass in run in Compile := Some("gui.GuiRunner")