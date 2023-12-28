package com.jobgun
package controller

import shared.backend as Backend // Imports:
import Backend.common.domain.ErrorResponse
import Backend.common.domain.responses.SuccessResponse
import Backend.common.utils.LRUCache
import Backend.common.model.{
  WeaviateReaderModel,
  CompletionModel,
  ResumeParserModel,
  EmbeddingModel
} 

import Backend.search.domain as SearchDomain // Imports:
import SearchDomain.*, SearchRoutes.*, requests.*, responses.*

import sttp.tapir as Tapir // Imports:
import Tapir.swagger.SwaggerUI
import Tapir.ztapir.RichZEndpoint
import Tapir.server.armeria.zio.ArmeriaZioServerInterpreter

// ZIO Imports:
import zio.*, zio.json.*

final class JobController(
    weaviateModel: WeaviateReaderModel,
    embeddingModel: EmbeddingModel,
    completionModel: CompletionModel
):

  given zio.Runtime[Any] = zio.Runtime.default

  def searchJobsWithEmbedding(
      request: JobSearchWithEmbeddingRequest
  ): IO[ErrorResponse, JobSearchFromEmbeddingResponse] = {
    for
      embeddedTitle <-
        if request.filters.preferredTitle.isEmpty then ZIO.succeed(None)
        else
          embeddingModel
            .embedText(
              request.filters.preferredTitle
            )
            .map(Some(_))
      jobs <- weaviateModel
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
    yield JobSearchFromEmbeddingResponse(jobs.toList)
  }.mapError(_ => ErrorResponse.InternalServerError)

  def searchJobsWithResume(
      request: JobSearchWithResumeRequest
  ): IO[ErrorResponse, JobSearchFromResumeResponse] = {
    val resumeFileType: Option["pdf" | "docx"] =
      request.file.fileName.flatMap(
        _.split('.').last match
          case e: ("pdf" | "docx") => Some(e)
          case _      => None
      )

    for
      parsedFile: String <- resumeFileType match
        case Some("pdf")  => ResumeParserModel.parsePDF(request.file.body)
        case Some("docx") => ResumeParserModel.parseWord(request.file.body)
        case _            => ZIO.fail(ErrorResponse.InternalServerError)
      parsedUser <- completionModel.parseUser(parsedFile)
      embeddedUser <- embeddingModel.embedText(parsedUser.toJson)
      jobs <- weaviateModel.searchJobs(0, 10, embeddedUser)
    yield JobSearchFromResumeResponse(jobs.toList, embeddedUser.toList)
  }.mapError(_ => ErrorResponse.InternalServerError)

  val jobSearchWithEmbeddingHandler =
    embeddingSearchRoute
      .zServerLogic[Any](searchJobsWithEmbedding)

  val jobSearchWithResumeHandler =
    resumeSearchRoute
      .zServerLogic[Any](searchJobsWithResume)

  val services = List(
    jobSearchWithEmbeddingHandler,
    jobSearchWithResumeHandler
  ).map(ArmeriaZioServerInterpreter().toService)
end JobController

object JobController:
  val default = (
    WeaviateReaderModel.default ++
      EmbeddingModel.default ++
      CompletionModel.default
  ) >>> ZLayer {
    for
      searchModel <- ZIO.service[WeaviateReaderModel]
      embeddingModel <- ZIO.service[EmbeddingModel]
      completionModel <- ZIO.service[CompletionModel]
    yield new JobController(
      searchModel,
      embeddingModel,
      completionModel
    )
  }
end JobController
