ThisBuild / scalaVersion     := "3.3.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.jobgun"
ThisBuild / organizationName := "jobgun"

lazy val backend = (project in file("backend"))
  .settings(
    name := "jobgun",
    libraryDependencies ++= Seq(
      Dependencies.zioDeps,
      Dependencies.tapirDeps,
      Dependencies.weaviateDeps
    ).flatten
  )
