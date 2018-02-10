name := "Cameltow"

version := "2.0-beta14"

organization := "se.chimps.cameltow"

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

credentials += Credentials(Path.userHome / ".ivy2" / ".cameltow")

publishTo := Some("se.chimps.cameltow" at "https://yamr.kodiak.se/maven")

publishArtifact in (Compile, packageDoc) := false

libraryDependencies += "io.undertow" % "undertow-servlet" % "1.4.22.Final"

libraryDependencies += "io.undertow" % "undertow-core" % "1.4.22.Final"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.1"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.22"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"