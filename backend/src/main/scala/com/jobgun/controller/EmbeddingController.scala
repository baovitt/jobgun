package com.jobgun.controller

// ZIO Imports:
import zio.*
import zio.json.*

// Jobgun Imports:
import com.jobgun.domain.requests.EmbedUserRequest
import com.jobgun.domain.responses.EmbedUserResponse
import com.jobgun.routes.EmbeddingRoutes.embedUserRoute
import com.jobgun.model.{EmbeddingModel, CompletionModel}
import com.jobgun.utils.LRUCache

// STTP Imports:
import sttp.model.StatusCode
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.ztapir.RichZEndpoint
import sttp.tapir.server.armeria.zio.ArmeriaZioServerInterpreter

final class EmbeddingController(
    embeddingModel: EmbeddingModel,
    completionModel: CompletionModel,
    cache: LRUCache[EmbedUserRequest, EmbedUserResponse]
)(using
    Runtime[Any]
):
  val embedUserHandler =
    embedUserRoute
      .zServerLogic[Any] { (req: EmbedUserRequest) =>
        (
          cache.get(req).catchAll(_ =>
            for
              parsedUser <- completionModel.parseUser(req.resume)
              embeddedUser <- embeddingModel.embedUser(parsedUser.toJson)
              response = EmbedUserResponse.fromEmbedding(embeddedUser)
              _ <- cache.put(req, response)
            yield response
          )
        ).mapError(_ => StatusCode.InternalServerError)
      }

  val services = List(
    embedUserHandler
  ).map(ArmeriaZioServerInterpreter().toService)
end EmbeddingController

object EmbeddingController:
  given zio.Runtime[Any] = zio.Runtime.default

  val default =
    (EmbeddingModel.default ++ CompletionModel.default ++ LRUCache
      .layer[EmbedUserRequest, EmbedUserResponse](1000)) >>> ZLayer {
      for
        embeddingModel <- ZIO.service[EmbeddingModel]
        completionModel <- ZIO.service[CompletionModel]
        cache <- ZIO.service[LRUCache[EmbedUserRequest, EmbedUserResponse]]
      yield new EmbeddingController(embeddingModel, completionModel, cache)
    }
end EmbeddingController
