name := "Cameltow"

version := "1.0"

scalaVersion := "2.11.1"

libraryDependencies += "io.undertow" % "undertow-servlet" % "1.0.10.Final"

libraryDependencies += "io.undertow" % "undertow-core" % "1.0.10.Final"

libraryDependencies += "com.typesafe" % "config" % "1.2.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.4"

libraryDependencies += "org.springframework" % "spring-context" % "4.0.4.RELEASE"

libraryDependencies += "com.google.inject" % "guice" % "3.0"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.1.5" % "test"