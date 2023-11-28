package com.jobgun.domain

object JobRoutes:

  // STTP Imports:
  import sttp.tapir.json.zio.jsonBody
  import sttp.tapir.ztapir.*

  // Jobgun Imports:
  import com.jobgun.domain.responses.*
  import com.jobgun.domain.requests.*

  // Java Imports:
  import java.io.File

  private val baseEndpoint =
    endpoint.in("api").in("v1").in("jobs")

  val jobSearchWithResumeRoute =
    baseEndpoint.post
      .in("resume")
      .in(multipartBody[JobSearchWithResumeRequest])
      .out(jsonBody[JobSearchFromResumeResponse])
      .errorOut(statusCode)
      .description("Searches jobs given a user's resume.")
      .name("searchJobs")

  val jobSearchWithEmbeddingRoute =
    baseEndpoint.post
      .in("embedding")
      .in(jsonBody[JobSearchWithEmbeddingRequest])
      .out(jsonBody[JobSearchFromEmbeddingResponse])
      .errorOut(statusCode)
      .description("Searches jobs given a user's resume embedding.")
      .name("searchJobs")

  val jobSearchWithDefaultRoute =
    baseEndpoint.post
      .in("default")
      .in(jsonBody[JobSearchWithDefaultRequest])
      .out(jsonBody[JobSearchFromResumeResponse])
      .errorOut(statusCode)
      .description("Searches jobs given a default resume.")
      .name("searchJobs")

  val endpoints = {
    val endpoints = List(
      jobSearchWithResumeRoute,
      jobSearchWithEmbeddingRoute,
      jobSearchWithDefaultRoute
    )

    endpoints.map(_.tags(List("Job Endpoints")))
  }
end JobRoutes
