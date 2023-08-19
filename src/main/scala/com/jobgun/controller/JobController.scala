package com.jobgun.controller

// ZIO Imports:
import zio.*

// Jobgun Imports:
import com.jobgun.domain.requests.JobSearchRequest
import com.jobgun.domain.responses.JobSearchResponse
import com.jobgun.routes.JobRoutes.jobSearchRoute
import com.jobgun.model.WeaviateSearchModel

// STTP Imports:
import sttp.model.StatusCode
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.ztapir.RichZEndpoint
import sttp.tapir.server.armeria.zio.ArmeriaZioServerInterpreter

final class JobController(weaviateSearchModel: WeaviateSearchModel):

  given zio.Runtime[Any] = zio.Runtime.default

  val jobSearchHandler =
    jobSearchRoute
      .zServerLogic[Any] { (req: JobSearchRequest) =>
        (weaviateSearchModel.searchJobs(req.embedding)
          <* ZIO.logInfo(s"Finished Job Search Route"))
          .map(jobs => JobSearchResponse.fromJobs(jobs))
          .mapError(_ => StatusCode.InternalServerError)
      }

  val services = List(
    jobSearchHandler
  ).map(ArmeriaZioServerInterpreter().toService)
end JobController

object JobController:
  val default = WeaviateSearchModel.default >>> ZLayer {
    for searchModel <- ZIO.service[WeaviateSearchModel]
    yield new JobController(searchModel)
  }
end JobController
