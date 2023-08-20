package com.jobgun.routes

object EmbeddingRoutes:

  // STTP Imports:
  import sttp.tapir.json.zio.jsonBody
  import sttp.tapir.ztapir.*

  // Jobgun Imports:
  import com.jobgun.shared.domain.requests.EmbedUserRequest
  import com.jobgun.shared.domain.responses.EmbedUserResponse

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
