package com.jobgun.shared.services

// Jobgun Imports:
import com.jobgun.shared.domain.requests.{EmbedUserRequest, JobSearchRequest}
import com.jobgun.shared.domain.responses.{JobSearchResponse, EmbedUserResponse}
import com.jobgun.shared.domain.routes.{JobRoutes, EmbeddingRoutes}

// ZIO Imports:
import zio.{IO, ZLayer, ZIO}

// Sttp Imports:
import sttp.tapir.client.sttp.SttpClientInterpreter
import sttp.model.{Uri, StatusCode}
import sttp.client3.impl.zio.FetchZioBackend
import sttp.tapir.DecodeResult

trait FetchService:
  def searchJobs(
      request: JobSearchRequest
  ): IO[StatusCode, JobSearchResponse]

  def embedUser(
      request: EmbedUserRequest
  ): IO[StatusCode, EmbedUserResponse]
end FetchService

object FetchService:

  private val backend = ZLayer.succeed(FetchZioBackend())

  val live = backend >>> ZLayer {
    for backend <- ZIO.service[FetchZioBackend]
    yield new FetchService:
      def searchJobs(
          request: JobSearchRequest
      ): IO[StatusCode, JobSearchResponse] =
        SttpClientInterpreter()
          .toRequest(
            JobRoutes.jobSearchRoute,
            Some(Uri("http://localhost:8080"))
          )
          .apply(request)
          .send(backend)
          .map(_.body)
          .catchAll { case _: Throwable => ZIO.fail(StatusCode.BadRequest) }
          .flatMap {
            case DecodeResult.Value(Right(response)) => ZIO.succeed(response)
            case _ => ZIO.fail(StatusCode.BadRequest)
          }

      def embedUser(
          request: EmbedUserRequest
      ): IO[StatusCode, EmbedUserResponse] =
        SttpClientInterpreter()
          .toRequest(
            EmbeddingRoutes.embedUserRoute,
            Some(Uri("http://localhost:8080"))
          )
          .apply(request)
          .send(backend)
          .map(_.body)
          .catchAll { case _: Throwable => ZIO.fail(StatusCode.BadRequest) }
          .flatMap {
            case DecodeResult.Value(Right(response)) => ZIO.succeed(response)
            case _ => ZIO.fail(StatusCode.BadRequest)
          }

  }
end FetchService
