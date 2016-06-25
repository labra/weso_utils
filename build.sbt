import sbt._
import sbt.Keys._

lazy val weso_utils = project.in(file("."))

organization := "es.weso"

name := "weso_utils"

version := "0.0.7"

scalaVersion := "2.11.8"

publishMavenStyle := true

libraryDependencies ++= Seq(
    "commons-configuration" % "commons-configuration" % "1.7"
  , "org.scala-lang" % "scala-compiler" % scalaVersion.value 
  , "io.argonaut" %% "argonaut" % "6.1"
  , "jline" % "jline" % "2.12.1"
  , "com.typesafe" % "config" % "1.0.1"
  , "junit" % "junit" % "4.10" % "test"
  , "org.scalactic" % "scalactic_2.11" % "2.2.4"
  , "org.scalatest" % "scalatest_2.11" % "2.2.4" 
  , "org.scalacheck" %% "scalacheck" % "1.12.4" 
  , "ch.qos.logback" %  "logback-classic" % "1.1.7"
  , "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0"
  )

// scalariformSettings

bintrayRepository in bintray := "weso-releases"

bintrayOrganization in bintray := Some("weso")

licenses += ("MPL-2.0", url("http://opensource.org/licenses/MPL-2.0"))

resolvers += Resolver.bintrayRepo("labra", "maven")

resolvers += Resolver.bintrayRepo("weso", "weso-releases")

