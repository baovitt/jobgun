package com.jobgun.controller

// Jobgun Imports:
import com.jobgun.shared.backend.domain.JobRoutes
import com.jobgun.shared.backend.auth.domain.AuthRoutes

// STTP Imports:
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.server.armeria.zio.ArmeriaZioServerInterpreter
import sttp.tapir.swagger.SwaggerUI
import sttp.apispec.openapi.circe.yaml.*

object OpenApiDocsController:
  given zio.Runtime[Any] = zio.Runtime.default

  private val jobEndpoints = JobRoutes.endpoints
  private val authEndpoints = AuthRoutes.endpoints

  private val endpoints = jobEndpoints ++ authEndpoints

  val docs =
    OpenAPIDocsInterpreter().toOpenAPI(endpoints, "Jobgun", "0.1.0")

  val docService =
    ArmeriaZioServerInterpreter().toService(SwaggerUI[zio.Task](docs.toYaml))
end OpenApiDocsController
