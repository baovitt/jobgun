package com.jobgun

// ZIO Imports:
import zio.*
import zio.openai.{Embeddings, Completions}
import zio.connect.file.LiveFileConnector
import zio.aws.s3.S3

// Armeria Imports:
import com.linecorp.armeria.server.Server

// Jobgun Imports:
import controller.{JobController, AuthController, RecruitController}
import shared.backend.common.model.{S3Model, EmailModel}
import shared.backend.common.config.{
  ResendConfig,
  PostgresConfig,
  ObjectStorageConfig
}

object Main extends ZIOAppDefault with Application:

  def run = logic.provide(
    JobController.default,
    AuthController.live,
    LiveFileConnector.layer,
    RecruitController.live,
    PostgresConfig.live,
    ResendConfig.live,
    EmailModel.resend,
    ObjectStorageConfig.live,
    ObjectStorageConfig.zioAwsConfig,
    S3.live,
    S3Model.layer,
    Embeddings.default,
    Completions.default,
    ZLayer.Debug.tree
  )

  val logic = ZIO.scoped {
    for
      server <- server.map(server =>
        ZIO.fromCompletableFuture(server.start().thenApply[Server](_ => server))
      )
      _ <- ZIO.scoped {
        ZIO.acquireRelease(server)(server =>
          ZIO.fromCompletableFuture(server.closeAsync()).orDie
        ) *> ZIO.never
      }.exitCode
    yield ()
  }

end Main
