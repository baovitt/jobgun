package com.jobgun.shared.backend
package search.domain

object SearchRoutes:

  // STTP Imports:
  import sttp.tapir.json.zio.jsonBody
  import sttp.tapir.ztapir.*
  import sttp.model.StatusCode

  // Jobgun Imports:
  import common.domain.responses.*
  import requests.*, responses.*
  import search.domain.ATSJobListing

  type JobId = Int

  private val searchEndpoint = 
    common.domain.baseEndpoint.in("search")

  val resumeSearchRoute =
    searchEndpoint.post
      .in("resume")
      .in(multipartBody[JobSearchWithResumeRequest])
      .out(jsonBody[JobSearchFromResumeResponse])
      .description("Searches jobs given a user's resume.")
      .name("searchJobs")

  val embeddingSearchRoute =
    searchEndpoint.post
      .in("embedding")
      .in(jsonBody[JobSearchWithEmbeddingRequest])
      .out(jsonBody[JobSearchFromEmbeddingResponse])
      .description("Searches jobs given a user's resume embedding.")
      .name("searchJobs")

  val applyRoute =
    searchEndpoint.post
      .in("apply")
      .in(jsonBody[JobApplyRequest])
      .securityIn(auth.bearer[String]())
      .out(jsonBody[SuccessResponse[Boolean]])
      .description("Searches jobs given a user's resume embedding.")
      .name("apply")

  val endpoints = {
    val endpoints = List(
      resumeSearchRoute,
      embeddingSearchRoute
    )

    endpoints.map(_.tags(List("Search Endpoints")))
  }
end SearchRoutes
