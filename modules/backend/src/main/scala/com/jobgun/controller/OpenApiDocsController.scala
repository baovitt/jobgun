package com.jobgun.controller

// Jobgun Imports:
import com.jobgun.shared.domain.routes.{JobRoutes, EmbeddingRoutes}

// STTP Imports:
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.server.armeria.zio.ArmeriaZioServerInterpreter
import sttp.tapir.swagger.SwaggerUI
import sttp.apispec.openapi.circe.yaml.*

object OpenApiDocsController:
  given zio.Runtime[Any] = zio.Runtime.default

  private val endpoints = JobRoutes.endpoints ++ EmbeddingRoutes.endpoints

  val docs =
    OpenAPIDocsInterpreter().toOpenAPI(endpoints, "Jobgun", "0.1.0")

  val docService =
    ArmeriaZioServerInterpreter().toService(SwaggerUI[zio.Task](docs.toYaml))
end OpenApiDocsController
