import sbt._

object Dependencies {
  lazy val tapirVersion = "1.0.2"
  lazy val ZIOConfigVersion = "3.0.1"

  lazy val zioDeps = Seq(
    "dev.zio" %% "zio" % "2.0.15",
    "dev.zio" %% "zio-openai" % "0.2.1",
    "dev.zio" %% "zio-config-typesafe" % ZIOConfigVersion,
    "dev.zio" %% "zio-config-magnolia" % ZIOConfigVersion,
  )

  lazy val tapirDeps = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-armeria-server-zio" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % tapirVersion,
  )

  lazy val weaviateDeps = Seq(
    "io.weaviate" % "client" % "4.2.1",
    "com.google.guava" % "guava" % "32.1.2-jre"
  )
}
