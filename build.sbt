import Dependencies._
import org.scalajs.linker.interface.ModuleSplitStyle

ThisBuild / scalaVersion     := "3.3.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.jobgun"
ThisBuild / organizationName := "jobgun"

val commonSttpZioVersion = "3.9.0"

lazy val shared = (project in file("modules/shared"))
  .settings(
    name := "jobgun",
    libraryDependencies ++= (
      Dependencies.backend.zioDeps ++
      Dependencies.backend.tapirDeps ++ 
      Dependencies.backend.weaviateDeps ++
      List(
        "org.apache.pdfbox" % "preflight" % "3.0.0",
        "org.apache.poi" % "poi-ooxml" % "5.2.3",
        "com.softwaremill.sttp.client3" %% "zio" % "3.9.1",
        "com.softwaremill.sttp.client3" %% "circe" % "3.9.1",
        "io.circe" %% "circe-generic" % "0.14.6",
        "io.getquill"          %% "quill-jdbc-zio" % "4.8.0",
        "org.postgresql"       %  "postgresql"     % "42.3.1",
        "com.github.jwt-scala" %% "jwt-zio-json" % "9.4.0",
        "dev.zio" %% "zio-connect-s3" % "0.4.4"
      )
    )
  )

lazy val backend = (project in file("modules/backend"))
  .settings(
    name := "jobgun",
    libraryDependencies ++= (
      Dependencies.backend.zioDeps ++
      Dependencies.backend.tapirDeps ++ 
      Dependencies.backend.weaviateDeps ++
      List(
        "org.apache.pdfbox" % "preflight" % "3.0.0",
        "org.apache.poi" % "poi-ooxml" % "5.2.3",
        "com.softwaremill.sttp.client3" %% "zio" % "3.9.1",
        "com.softwaremill.sttp.client3" %% "circe" % "3.9.1",
        "io.circe" %% "circe-generic" % "0.14.6",
        "io.getquill"          %% "quill-jdbc-zio" % "4.8.0",
        "org.postgresql"       %  "postgresql"     % "42.3.1",
        "com.github.jwt-scala" %% "jwt-zio-json" % "9.4.0",
        "dev.zio" %% "zio-connect-s3" % "0.4.4",
        "dev.zio" %% "zio-connect-file" % "0.4.4"
      )
    ),
    fork := true,
    scalacOptions ++= Seq(
      "-Xmax-inlines", "64",
    ),
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", "versions", "9", "module-info.class") => MergeStrategy.first
      case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.concat
      case PathList("META-INF", "native", "lib", xs @ _*) => MergeStrategy.first
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case "application.conf" => MergeStrategy.concat
      case PathList("deriving.conf") => MergeStrategy.concat
      case "module-info.class" => MergeStrategy.first
      
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    assembly / assemblyJarName := "jobgun.jar"
  )
  .dependsOn(shared)

lazy val pipeline = (project in file("modules/pipeline"))
  .settings(
    name := "jobgun",
    libraryDependencies ++= (
      Dependencies.backend.zioDeps ++
      Dependencies.backend.tapirDeps ++ 
      Dependencies.backend.weaviateDeps ++
      List(
        "org.apache.pdfbox" % "preflight" % "3.0.0",
        "org.apache.poi" % "poi-ooxml" % "5.2.3",
        "com.softwaremill.sttp.client3" %% "zio" % "3.9.1",
        "com.softwaremill.sttp.client3" %% "circe" % "3.9.1",
        "io.circe" %% "circe-generic" % "0.14.6",
        "io.getquill"          %% "quill-jdbc-zio" % "4.8.0",
        "org.postgresql"       %  "postgresql"     % "42.3.1",
        "com.github.jwt-scala" %% "jwt-zio-json" % "9.4.0",
        "dev.zio" %% "zio-connect-s3" % "0.4.4"
      )
    ),
    fork := true,
    scalacOptions ++= Seq(
      "-Xmax-inlines", "64",
    ),
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", "versions", "9", "module-info.class") => MergeStrategy.first
      case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.concat
      case PathList("META-INF", "native", "lib", xs @ _*) => MergeStrategy.first
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case "application.conf" => MergeStrategy.concat
      case PathList("deriving.conf") => MergeStrategy.concat
      case "module-info.class" => MergeStrategy.first
      
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    assembly / assemblyJarName := "jobgun.jar"
  )
  .dependsOn(shared)

// lazy val frontend = project
//   .in(file("modules/frontend"))
//   .enablePlugins(ScalaJSPlugin)
//   .settings(
//     name := "jobgun",
//     libraryDependencies ++= Seq(
//       "com.raquo" %%% "laminar" % "16.0.0",
//       "io.frontroute" %%% "frontroute" % "0.18.0",
//       "be.doeraene" %%% "web-components-ui5" % "1.17.1",
//       "dev.zio" %%% "zio-json" % "0.6.2",
//     )
//   )
//   .dependsOn(shared.js)

// lazy val website = project
//   .in(file("modules/website"))
//   .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
//   .settings(
//     name := "jobgun",
//     libraryDependencies ++= Seq(
//       "com.raquo" %%% "laminar" % "16.0.0",
//       "io.frontroute" %%% "frontroute" % "0.18.0",
//       "dev.zio" %%% "zio-json" % "0.6.2",
//     ),
//     scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
//     // scalaJSLinkerConfig ~= { _.withModuleSplitStyle(ModuleSplitStyle.FewestModules) },
//     scalaJSLinkerConfig ~= { _.withSourceMap(false) },
//     scalaJSUseMainModuleInitializer := true,
//     npmDependencies in Compile ++= Seq(
//       "@ui5/webcomponents" -> "1.17.1",
//       "@ui5/webcomponents-fiori" -> "1.17.1",
//       "@ui5/webcomponents-icons" -> "1.17.1",
//     )
//   )
//   .dependsOn(frontend)
