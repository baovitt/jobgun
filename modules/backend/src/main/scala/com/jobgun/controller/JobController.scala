package com.jobgun.controller

// ZIO Imports:
import zio.*
import zio.json.*

// Jobgun Imports:

import com.jobgun.domain.requests.JobSearchWithResumeRequest
import com.jobgun.shared.domain.requests.JobRequest
import com.jobgun.shared.domain.responses.JobResponse
import com.jobgun.shared.utils.LRUCache
import com.jobgun.domain.JobRoutes.{
  jobSearchWithEmbeddingRoute,
  jobSearchWithResumeRoute
}
import com.jobgun.model.{
  WeaviateSearchModel,
  ResumeParserModel,
  EmbeddingModel,
  CompletionModel
}

// STTP Imports:

import sttp.model.StatusCode
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.ztapir.RichZEndpoint
import sttp.tapir.server.armeria.zio.ArmeriaZioServerInterpreter

final class JobController(
    weaviateSearchModel: WeaviateSearchModel,
    embeddingModel: EmbeddingModel,
    completionModel: CompletionModel,
    embeddingRequestCache: LRUCache[
      JobRequest.JobSearchWithEmbeddingRequest,
      JobResponse.JobSearchFromEmbeddingResponse
    ]
):

  given zio.Runtime[Any] = zio.Runtime.default

  def searchJobsWithEmbedding(
      request: JobRequest.JobSearchWithEmbeddingRequest
  ): IO[StatusCode, JobResponse.JobSearchFromEmbeddingResponse] =
    embeddingRequestCache
      .get(request)
      .catchAll(_ =>
        for
          jobs <- weaviateSearchModel
            .searchJobs(request.page, request.pageSize, request.embedding)
          response = JobResponse.JobSearchFromEmbeddingResponse(jobs)
          _ <- embeddingRequestCache.put(request, response)
        yield response
      )
      .mapError(_ => StatusCode.InternalServerError)

  def searchJobsWithResume(
      request: JobSearchWithResumeRequest
  ): IO[StatusCode, JobResponse.JobSearchFromResumeResponse] = {
    val resumeFileType: Option["pdf" | "docx"] =
      request.file.fileName.flatMap(
        _.split('.').last match
          case "pdf"  => Some("pdf")
          case "docx" => Some("docx")
          case _      => None
      )

    for
      parsedFile <- resumeFileType match
        case Some("pdf")  => ResumeParserModel.parsePDF(request.file.body)
        case Some("docx") => ResumeParserModel.parseWord(request.file.body)
        case _            => ZIO.fail(StatusCode.InternalServerError)
      parsedUser <- completionModel.parseUser(parsedFile)
      embeddedUser <- embeddingModel.embedUser(parsedUser.toJson)
      jobs <- weaviateSearchModel.searchJobs(0, 25, embeddedUser)
      response = JobResponse.JobSearchFromResumeResponse(jobs, embeddedUser)
    yield response
  }.mapError(_ => StatusCode.InternalServerError)

  val jobSearchWithEmbeddingHandler =
    jobSearchWithEmbeddingRoute
      .zServerLogic[Any] { searchJobsWithEmbedding }

  val jobSearchWithResumeHandler =
    jobSearchWithResumeRoute
      .zServerLogic[Any] { searchJobsWithResume }

  val services = List(
    jobSearchWithEmbeddingHandler,
    jobSearchWithResumeHandler
  ).map(ArmeriaZioServerInterpreter().toService)
end JobController

object JobController:
  val default = (
    WeaviateSearchModel.default ++
      EmbeddingModel.default ++
      CompletionModel.default ++
      LRUCache.layer[
        JobRequest.JobSearchWithEmbeddingRequest,
        JobResponse.JobSearchFromEmbeddingResponse
      ](1000)
  ) >>> ZLayer {
    for
      searchModel <- ZIO.service[WeaviateSearchModel]
      embeddingModel <- ZIO.service[EmbeddingModel]
      completionModel <- ZIO.service[CompletionModel]
      embeddingRequestCache <- ZIO.service[LRUCache[
        JobRequest.JobSearchWithEmbeddingRequest,
        JobResponse.JobSearchFromEmbeddingResponse
      ]]
    yield new JobController(
      searchModel,
      embeddingModel,
      completionModel,
      embeddingRequestCache
    )
  }
end JobController
