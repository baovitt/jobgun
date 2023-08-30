package com.jobgun

// Jobgun Imports:
import com.jobgun.controller.{JobController, OpenApiDocsController}
import com.jobgun.utils.extensions.ArmeriaServerExtensions.services

// Armeria Imports:
import com.linecorp.armeria.server.Server

// Java Imports:
import java.time.Duration

// ZIO Imports
import zio.ZIO

trait Application:
  val server =
    for
      jobController <- ZIO.service[JobController]
      services = jobController.services
    yield Server
      .builder()
      .http(8080)
      .services(services)
      .service(OpenApiDocsController.docService)
      .requestTimeout(Duration.ofSeconds(30))
      .build()
end Application
