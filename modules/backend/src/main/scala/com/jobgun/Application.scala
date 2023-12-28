package com.jobgun

// Jobgun Imports:
import com.jobgun.controller.{
  JobController,
  AuthController,
  RecruitController,
  OpenApiDocsController
}
import com.jobgun.utils.extensions.ArmeriaServerExtensions.services

// Armeria Imports:
import com.linecorp.armeria.server.Server

// Java Imports:
import java.time.Duration

// ZIO Imports
import zio.ZIO

trait Application:
  def server =
    for
      jobController <- ZIO.service[JobController]
      authController <- ZIO.service[AuthController]
      recruitController <- ZIO.service[RecruitController]
    yield Server
      .builder()
      .http(8080)
      .services(jobController.services*)
      .services(authController.services*)
      .services(recruitController.services*)
      .service(OpenApiDocsController.docService)
      .requestTimeout(Duration.ofSeconds(30))
      .build()
end Application
