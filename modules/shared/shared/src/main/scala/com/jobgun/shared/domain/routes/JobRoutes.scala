package com.jobgun.shared.domain.routes

object JobRoutes:

  // STTP Imports:
  import sttp.tapir.json.zio.jsonBody
  import sttp.tapir.ztapir.*

  // Jobgun Imports:
  import com.jobgun.shared.domain.requests.JobSearchRequest
  import com.jobgun.shared.domain.responses.JobSearchResponse

  private val baseEndpoint =
    endpoint.in("api").in("v1").in("jobs")

  val jobSearchRoute =
    baseEndpoint.post
      .in(jsonBody[JobSearchRequest])
      .out(jsonBody[JobSearchResponse])
      .errorOut(statusCode)
      .description("Searches jobs given a user's resume embedding.")
      .name("searchJobs")

  val endpoints = {
    val endpoints = List(
      jobSearchRoute
    )

    endpoints.map(_.tags(List("Job Endpoints")))
  }
end JobRoutes
