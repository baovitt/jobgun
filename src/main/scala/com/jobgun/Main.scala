package com.jobgun

// ZIO Imports:
import zio.*
import zio.openai.{Embeddings, Completions}

// Armeria Imports:
import com.linecorp.armeria.server.Server

// Jobgun Imports:
import com.jobgun.config.{HttpServerSettings, HttpConfig}
import com.jobgun.controller.{EmbeddingController, JobController}

import sttp.tapir.ztapir.*

object Main extends ZIOAppDefault with Application:

  def run = logic.provide(
    EmbeddingController.default,
    JobController.default,
    HttpConfig.live,
    HttpServerSettings.default,
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
