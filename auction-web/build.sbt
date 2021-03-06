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
  "com.google.guava" % "guava" % "27.1-jre",
  "org.apache.httpcomponents" % "httpclient" % "4.5.12",
  "io.swagger" %% "swagger-play2" % "1.7.1",
  "junit" % "junit" % "4.11" % Test,
  "org.testcontainers" % "mongodb" % "1.14.3" % Test,
  "org.mockito" % "mockito-all" % "1.10.19" % Test
)

jacocoExcludes in Test := Seq(
  "controllers.Reverse*",
  "controllers.javascript.*",
  "model.*",
  "Module",
  "router.Routes*",
  "*.routes*"
)
