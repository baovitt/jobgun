import Dependencies._
import org.scalajs.linker.interface.ModuleSplitStyle

ThisBuild / scalaVersion     := "3.3.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.jobgun"
ThisBuild / organizationName := "jobgun"

lazy val shared = (crossProject(JSPlatform, JVMPlatform) in file("modules/shared"))
  .settings(
    name := "jobgun",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %%% "tapir-json-zio" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % tapirVersion,
      "com.softwaremill.sttp.tapir" %%% "tapir-zio" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-zio" % tapirVersion
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.client3" %%% "zio" % "3.9.0",
      "com.softwaremill.sttp.tapir" %%% "tapir-sttp-client" % tapirVersion,
      "io.github.cquiroz" %%% "scala-java-time" % "2.5.0-M2"
    )
  )

lazy val backend = (project in file("modules/backend"))
  .settings(
    name := "jobgun",
    libraryDependencies ++= Seq(
      Dependencies.backend.zioDeps ++
      Dependencies.backend.tapirDeps ++ 
      Dependencies.backend.weaviateDeps
    ).flatten,
    fork := true
  )
  .dependsOn(shared.jvm)

lazy val frontend = project
  .in(file("modules/frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "jobgun",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %%% "tapir-json-zio" % tapirVersion,
      "com.raquo" %%% "laminar" % "16.0.0",
      "io.frontroute" %%% "frontroute" % "0.18.0",
      "com.softwaremill.sttp.client4" %%% "zio" % "4.0.0-M4"
    )
  )
  .dependsOn(shared.js)

lazy val website = project
  .in(file("modules/website"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "jobgun",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %%% "tapir-json-zio" % tapirVersion,
      "com.raquo" %%% "laminar" % "16.0.0",
      "io.frontroute" %%% "frontroute" % "0.18.0",
      "com.softwaremill.sttp.client4" %%% "zio" % "4.0.0-M4"
    ),
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
    scalaJSLinkerConfig ~= { _.withModuleSplitStyle(ModuleSplitStyle.FewestModules) },
    scalaJSLinkerConfig ~= { _.withSourceMap(false) },
    scalaJSUseMainModuleInitializer := true
  )
  .dependsOn(frontend)