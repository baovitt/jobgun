package com.jobgun.controller

// ZIO Imports:
import zio.*
import zio.json.*

// Jobgun Imports:
import com.jobgun.domain.requests.EmbedUserRequest
import com.jobgun.domain.responses.EmbedUserResponse
import com.jobgun.routes.EmbeddingRoutes.embedUserRoute
import com.jobgun.model.{EmbeddingModel, CompletionModel}

// STTP Imports:
import sttp.model.StatusCode
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.ztapir.RichZEndpoint
import sttp.tapir.server.armeria.zio.ArmeriaZioServerInterpreter

final class EmbeddingController(
    embeddingModel: EmbeddingModel,
    completionModel: CompletionModel
)(using
    Runtime[Any]
):
  val embedUserHandler =
    embedUserRoute
      .zServerLogic[Any] { (req: EmbedUserRequest) =>
        (
          for
            parsedUser <- completionModel.parseUser(req.resume)
            embeddedUser <- embeddingModel.embedUser(parsedUser.toJson)
          yield embeddedUser
        ).map(EmbedUserResponse.fromEmbedding)
          .mapError(_ => StatusCode.InternalServerError)
      }

  val services = List(
    embedUserHandler
  ).map(ArmeriaZioServerInterpreter().toService)
end EmbeddingController

object EmbeddingController:
  given zio.Runtime[Any] = zio.Runtime.default

  val default =
    (EmbeddingModel.default ++ CompletionModel.default) >>> ZLayer {
      for
        embeddingModel <- ZIO.service[EmbeddingModel]
        completionModel <- ZIO.service[CompletionModel]
      yield new EmbeddingController(embeddingModel, completionModel)
    }
end EmbeddingController
