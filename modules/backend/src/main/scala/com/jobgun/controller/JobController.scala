package com.jobgun.controller

// ZIO Imports:
import zio.*
import zio.json.*

// Jobgun Imports:

import com.jobgun.shared.backend.domain.ParsedJobDescription
import com.jobgun.shared.backend.domain.requests.*
import com.jobgun.shared.backend.domain.responses.*
import com.jobgun.shared.backend.utils.LRUCache
import com.jobgun.shared.backend.domain.JobRoutes.{
  jobSearchWithEmbeddingRoute,
  jobSearchWithResumeRoute,
  jobSearchWithDefaultRoute
}
import com.jobgun.shared.backend.model.{
  WeaviateModel,
  ResumeParserModel,
  CompletionModel
}
import com.jobgun.shared.backend.model.EmbeddingModel

// STTP Imports:

import sttp.model.StatusCode
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.ztapir.RichZEndpoint
import sttp.tapir.server.armeria.zio.ArmeriaZioServerInterpreter

final class JobController(
    WeaviateModel: WeaviateModel,
    embeddingModel: EmbeddingModel,
    completionModel: CompletionModel,
    embeddingRequestCache: LRUCache[
      JobSearchWithEmbeddingRequest,
      JobSearchFromEmbeddingResponse
    ]
):

  given zio.Runtime[Any] = zio.Runtime.default

  def searchJobsWithEmbedding(
      request: JobSearchWithEmbeddingRequest
  ): IO[StatusCode, JobSearchFromEmbeddingResponse] =
    embeddingRequestCache
      .get(request)
      .catchAll(_ =>
        for
          embeddedTitle <-
            if request.filters.preferredTitle.isEmpty then ZIO.succeed(None)
            else
              embeddingModel
                .embedText(
                  request.filters.preferredTitle
                )
                .map(Some(_))
          jobs <- WeaviateModel
            .searchJobs(
              request.page,
              10,
              embeddedTitle match
                case Some(embeddedTitle) =>
                  request.embedding
                    .map(_ * 0.80)
                    .zip(embeddedTitle.map(_ / 5))
                    .map(_ + _)
                case None => request.embedding
              ,
              Some(request.filters)
            )
          response = JobSearchFromEmbeddingResponse(jobs.toList)
          _ <- embeddingRequestCache.put(request, response)
        yield response
      )
      .mapError(_ => StatusCode.InternalServerError)

  def searchJobsWithDefault(
      request: JobSearchWithDefaultRequest
  ): IO[StatusCode, JobSearchFromResumeResponse] = {
    for
      parsedUser <- request.default match
        case "network-engineer" =>
          ZIO.succeed(ParsedJobDescription.defaultNetworkEngineer)
        case "construction-manager" =>
          ZIO.succeed(ParsedJobDescription.defaultConstructionManager)
        case "react-developer" =>
          ZIO.succeed(ParsedJobDescription.defaultReactDeveloper)
        case "devops-engineer" =>
          ZIO.succeed(ParsedJobDescription.defaultDevops)
        case _ => ZIO.fail(StatusCode.InternalServerError)
      embeddedUser <- embeddingModel.embedText(parsedUser.toJson)
      jobs <- WeaviateModel.searchJobs(0, 25, embeddedUser)
      response = JobSearchFromResumeResponse(jobs.toList, embeddedUser.toList)
    yield response
  }.mapError(_ => StatusCode.InternalServerError)

  def searchJobsWithResume(
      request: JobSearchWithResumeRequest
  ): IO[StatusCode, JobSearchFromResumeResponse] = {
    val resumeFileType: Option["pdf" | "docx"] =
      request.file.fileName.flatMap(
        _.split('.').last match
          case "pdf"  => Some("pdf")
          case "docx" => Some("docx")
          case _      => None
      )

    for
      parsedFile: String <- resumeFileType match
        case Some("pdf")  => ResumeParserModel.parsePDF(request.file.body)
        case Some("docx") => ResumeParserModel.parseWord(request.file.body)
        case _            => ZIO.fail(StatusCode.InternalServerError)
      _ <- zio.Console.printLine("parsedFile")
      parsedUser <- completionModel.parseUser(parsedFile)
      _ <- zio.Console.printLine("parsedUser")
      embeddedUser <- embeddingModel.embedText(parsedUser.toJson)
      _ <- zio.Console.printLine("embeddedUser")
      jobs <- WeaviateModel.searchJobs(0, 10, embeddedUser)
      _ <- zio.Console.printLine("jobs")
      response = JobSearchFromResumeResponse(jobs.toList, embeddedUser.toList)
    yield response
  }.mapError(_ => StatusCode.InternalServerError)

  val jobSearchWithEmbeddingHandler =
    jobSearchWithEmbeddingRoute
      .zServerLogic[Any](searchJobsWithEmbedding)

  val jobSearchWithResumeHandler =
    jobSearchWithResumeRoute
      .zServerLogic[Any](searchJobsWithResume)

  val jobSearchWithDefaultHandler =
    jobSearchWithDefaultRoute
      .zServerLogic[Any](searchJobsWithDefault)

  val services = List(
    jobSearchWithEmbeddingHandler,
    jobSearchWithResumeHandler,
    jobSearchWithDefaultHandler
  ).map(ArmeriaZioServerInterpreter().toService)
end JobController

object JobController:
  val default = (
    WeaviateModel.default ++
      EmbeddingModel.default ++
      CompletionModel.default ++
      LRUCache.layer[
        JobSearchWithEmbeddingRequest,
        JobSearchFromEmbeddingResponse
      ](1000)
  ) >>> ZLayer {
    for
      searchModel <- ZIO.service[WeaviateModel]
      embeddingModel <- ZIO.service[EmbeddingModel]
      completionModel <- ZIO.service[CompletionModel]
      embeddingRequestCache <- ZIO.service[LRUCache[
        JobSearchWithEmbeddingRequest,
        JobSearchFromEmbeddingResponse
      ]]
    yield new JobController(
      searchModel,
      embeddingModel,
      completionModel,
      embeddingRequestCache
    )
  }
end JobController
