package com.jobgun
package controller

import shared.backend as Backend // Imports:
import Backend.search.domain.SearchRoutes
import Backend.profile.domain.ProfileRoutes
import Backend.recruit.domain.RecruitRoutes

import sttp.tapir as Tapir // Imports:
import Tapir.docs.openapi.OpenAPIDocsInterpreter
import Tapir.server.armeria.zio.ArmeriaZioServerInterpreter
import Tapir.swagger.SwaggerUI

// STTP ApiSpec Imports:
import sttp.apispec.openapi.circe.yaml.RichOpenAPI

object OpenApiDocsController:
  given zio.Runtime[Any] = zio.Runtime.default

  private val endpoints =
    SearchRoutes.endpoints ++ ProfileRoutes.endpoints ++ RecruitRoutes.endpoints

  val interpreter =
    OpenAPIDocsInterpreter().toOpenAPI(endpoints, "Jobgun", "0.1.0")

  val docService =
    ArmeriaZioServerInterpreter().toService(
      SwaggerUI[zio.Task](interpreter.toYaml)
    )
end OpenApiDocsController
