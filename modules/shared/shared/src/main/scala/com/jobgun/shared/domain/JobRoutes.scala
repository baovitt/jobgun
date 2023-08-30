package com.jobgun.shared.domain.routes

object JobRoutes:

  // STTP Imports:
  import sttp.tapir.json.zio.jsonBody
  import sttp.tapir.ztapir.*

  // Jobgun Imports:
  import com.jobgun.shared.domain.requests.JobRequest
  import com.jobgun.shared.domain.responses.JobResponse

  private val baseEndpoint =
    endpoint.in("api").in("v1").in("jobs")

  val jobSearchWithResumeRoute =
    baseEndpoint.post.in("resume")
      .in(multipartBody[JobRequest.JobSearchWithResumeRequest])
      .out(jsonBody[JobResponse.JobSearchFromResumeResponse])
      .errorOut(statusCode)
      .description("Searches jobs given a user's resume.")
      .name("searchJobs")

  val jobSearchWithEmbeddingRoute =
    baseEndpoint.post.in("embedding")
      .in(jsonBody[JobRequest.JobSearchWithEmbeddingRequest])
      .out(jsonBody[JobResponse.JobSearchFromEmbeddingResponse])
      .errorOut(statusCode)
      .description("Searches jobs given a user's resume embedding.")
      .name("searchJobs")

  val endpoints = {
    val endpoints = List(
      jobSearchWithResumeRoute,
      jobSearchWithEmbeddingRoute
    )

    endpoints.map(_.tags(List("Job Endpoints")))
  }
end JobRoutes
