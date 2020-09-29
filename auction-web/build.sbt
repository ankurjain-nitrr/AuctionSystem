name := "auction-web"
organization := "ankur"
maintainer := "ankurjain.nitrr@gmail.com"
version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  guice,
  javaWs,
  "org.json" % "json" % "20200518",
  "io.fastjson" % "boon" % "0.34",
  "org.projectlombok" % "lombok" % "1.18.12",
  "io.micrometer" % "micrometer-registry-prometheus" % "1.1.1",
  "org.mongodb" % "mongo-java-driver" % "3.12.7",
  "com.google.guava" % "guava" % "29.0-jre",
  "junit" % "junit" % "4.11" % Test,
  "org.testcontainers" % "mongodb" % "1.14.3" % Test
)
