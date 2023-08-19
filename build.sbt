import Dependencies._

ThisBuild / scalaVersion     := "3.3.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.jobgun"
ThisBuild / organizationName := "jobgun"

lazy val tapirVersion = "1.0.2"
lazy val ZIOConfigVersion = "3.0.1"

lazy val root = (project in file("."))
  .settings(
    name := "jobgun",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.15",
      "dev.zio" %% "zio-http" % "3.0.0-RC2",
      "dev.zio" %% "zio-openai" % "0.2.1",
      "com.softwaremill.sttp.tapir" %% "tapir-armeria-server-zio" % "1.6.4",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % tapirVersion,
      "io.d11" %% "zhttp" % "2.0.0-RC10",
      "dev.zio" %% "zio-config-typesafe" % ZIOConfigVersion,
      "dev.zio" %% "zio-config-magnolia" % ZIOConfigVersion,
      "io.weaviate" % "client" % "4.2.1",
      "com.google.guava" % "guava" % "32.1.2-jre"
    )
  )
