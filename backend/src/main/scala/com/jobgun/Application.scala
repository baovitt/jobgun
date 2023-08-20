package com.jobgun

// Jobgun Imports:
import com.jobgun.controller.{
  JobController,
  EmbeddingController,
  OpenApiDocsController
}
import com.jobgun.extensions.ArmeriaServerExtensions.services

// Armeria Imports:
import com.linecorp.armeria.server.Server

import zio.ZIO

trait Application:
  val server = (port: Short) =>
    for
      embeddingController <- ZIO.service[EmbeddingController]
      jobController <- ZIO.service[JobController]
      services =
        embeddingController.services ++ jobController.services
    yield Server
      .builder()
      .http(port)
      .services(services)
      .service(OpenApiDocsController.docService)
      .build()
end Application
