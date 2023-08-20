package com.jobgun.controller

// ZIO Imports:
import zio.*

// Jobgun Imports:
import com.jobgun.domain.requests.JobSearchRequest
import com.jobgun.domain.responses.JobSearchResponse
import com.jobgun.routes.JobRoutes.jobSearchRoute
import com.jobgun.model.WeaviateSearchModel
import com.jobgun.utils.LRUCache

// STTP Imports:
import sttp.model.StatusCode
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.ztapir.RichZEndpoint
import sttp.tapir.server.armeria.zio.ArmeriaZioServerInterpreter

final class JobController(
  weaviateSearchModel: WeaviateSearchModel,
  cache: LRUCache[JobSearchRequest, JobSearchResponse]
):

  given zio.Runtime[Any] = zio.Runtime.default

  val jobSearchHandler =
    jobSearchRoute
      .zServerLogic[Any] { (req: JobSearchRequest) =>
        cache.get(req).catchAll(_ =>
          for
            jobs <- weaviateSearchModel.searchJobs(req.page, req.pageSize, req.embedding)
            response = JobSearchResponse.fromJobs(jobs)
            _ <- cache.put(req, response)
          yield response
        ).mapError(_ => StatusCode.InternalServerError)
      }

  val services = List(
    jobSearchHandler
  ).map(ArmeriaZioServerInterpreter().toService)
end JobController

object JobController:
  val default = (WeaviateSearchModel.default ++ LRUCache
      .layer[JobSearchRequest, JobSearchResponse](1000)) >>> ZLayer {
    for 
      searchModel <- ZIO.service[WeaviateSearchModel]
      cache <- ZIO.service[LRUCache[JobSearchRequest, JobSearchResponse]]
    yield new JobController(searchModel, cache)
  }
end JobController
