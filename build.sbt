name := """ripper"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "3.0.1" % "test",
    "org.scaldi" %% "scaldi" % "0.5.5",
    "org.scalafx" %% "scalafx" % "2.2.76-R11"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

unmanagedJars in Compile +=
  Attributed.blank(
      file(scala.util.Properties.javaHome) / "/lib/jfxrt.jar")

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.3"
