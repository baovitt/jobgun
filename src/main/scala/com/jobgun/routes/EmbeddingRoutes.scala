package com.jobgun.routes

import zio.*
import sttp.apispec.openapi.circe.yaml.*
import sttp.model.StatusCode
import sttp.tapir.PublicEndpoint
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.json.zio.*
// import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.ztapir.*
import zhttp.http.{Http, HttpApp, Request, Response}

object EmbeddingRoutes:

  // STTP Imports:
  import sttp.tapir.json.zio.jsonBody
  import sttp.tapir.ztapir.*

  // Jobgun Imports:
  import com.jobgun.domain.requests.EmbedUserRequest
  import com.jobgun.domain.responses.EmbedUserResponse

  private val baseEndpoint =
    endpoint.in("api").in("v1").in("embedding")

  val embedUserRoute =
    baseEndpoint.post
      .in(jsonBody[EmbedUserRequest])
      .out(jsonBody[EmbedUserResponse])
      .errorOut(statusCode)
      .description("Embeds a user's resume.")
      .name("embedUser")

  val endpoints = {
    val endpoints = List(
      embedUserRoute
    )

    endpoints.map(_.tags(List("Embedding Endpoints")))
  }

end EmbeddingRoutes
