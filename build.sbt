name := "Cameltow"

version := "2.0-beta13"

organization := "se.chimps.cameltow"

credentials += Credentials(Path.userHome / ".ivy2" / ".cameltow")

publishTo := Some("se.chimps.cameltow" at "https://yamr.kodiak.se/maven")

publishArtifact in (Compile, packageDoc) := false

scalaVersion := "2.11.8"

libraryDependencies += "io.undertow" % "undertow-servlet" % "1.3.27.Final"

libraryDependencies += "io.undertow" % "undertow-core" % "1.3.27.Final"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.1"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.22"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"