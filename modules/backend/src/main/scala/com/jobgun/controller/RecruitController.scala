package com.jobgun
package controller

// ZIO Imports:
import zio.*
import zio.json.*
import zio.stream.*
import zio.connect.s3.*
import zio.connect.file.FileConnector

// Jobgun Imports:
import shared.backend as Backend

import Backend.recruit.model.*
import Backend.recruit.domain.{requests as recruitRequests, *}
import recruitRequests.*, RecruitRoutes.*, sql.RecruiterAccountTable

import Backend.common.domain.{responses as commonResponses, *}
import commonResponses.{SuccessResponse, JwtResponse}
import Backend.common.model.{EmailModel, S3Model}
import Backend.common.model.auth.JWTModel
import Backend.common.utils.ExpiringCache

// STTP Imports:
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.ztapir.RichZEndpoint
import sttp.tapir.server.armeria.zio.ArmeriaZioServerInterpreter

// JAVA Imports:
import java.sql.SQLException
import java.util.UUID

final class RecruitController(
    emailModel: RecruiterEmailModel,
    recruiterModel: RecruiterPostgresModel[Any, SQLException],
    integrationModel: IntegrationPostgresModel[Any, SQLException],
    nonceCache: ExpiringCache[Nonce, Nonce.RecruiterId],
    s3Model: S3Model,
    fileConnector: FileConnector
):
  given zio.Runtime[Any] = zio.Runtime.default

  private def join(
      request: SignUpRequest
  ): IO[ErrorResponse, SuccessResponse[Boolean]] = {
    def sendVerificationEmail(id: Int) =
      val verificationUUID = UUID.randomUUID().toString
      for
        _ <- nonceCache.put(Nonce.SignUpNonce(verificationUUID), id)
        _ <- emailModel.sendVerificationEmail(request.email, verificationUUID)
      yield SuccessResponse(true)

    for
      // If the user already exists and isn't verified, send a new verification email.
      userOption <- recruiterModel.userExists(email = request.email)
      response <- userOption match
        case Some(user) if user.isVerified =>
          ZIO.fail(ErrorResponse.BadRequest("User already exists"))
        case Some(user) => sendVerificationEmail(user.id)
        case None =>
          for
            fileLocation <- S3Model.FileLocation.applyZIO(
              S3Model.resumesBucket,
              request.fileExtension,
              S3Model.S3Folder.RecruiterFolder
            )
            _ <- s3Model.putFile(request.resume.body, fileLocation)
            userId <- recruiterModel.create(
              firstName = request.firstName,
              lastName = request.lastName,
              email = request.email,
              password = request.password,
              company = request.company,
              companyUrl = request.companyUrl,
              resumeUrl =
                s"https://jobgun-resumes.nyc3.cdn.digitaloceanspaces.com/recruit/${fileLocation.fileName}.${fileLocation.fileExtension}"
            )
            response <- sendVerificationEmail(userId)
          yield response
    yield response
  }.mapError {
    case e: ErrorResponse => e
    case _                => ErrorResponse.InternalServerError
  }

  // If the user already exists and isn't verified, send a new verification email.
  private def resend(
      request: ResendVerificationEmailRequest
  ): IO[ErrorResponse, SuccessResponse[Boolean]] = {
    for
      userOption <- recruiterModel.userExists(email = request.email)
      response <- userOption match
        case Some(user) if user.isVerified =>
          ZIO.fail(ErrorResponse.Unauthorized("endpoint"))
        case Some(user) =>
          val verificationUUID = UUID.randomUUID().toString
          for
            _ <- nonceCache.put(Nonce.SignUpNonce(verificationUUID), user.id)
            _ <- emailModel.sendVerificationEmail(
              request.email,
              verificationUUID
            )
          yield SuccessResponse(true)
        case None => ZIO.fail(ErrorResponse.NotFound("no-account"))
    yield response
  }.mapError {
    case e: ErrorResponse => e
    case _                => ErrorResponse.InternalServerError
  }

  private def verify(token: String): IO[ErrorResponse, String] = {
    for
      userId <- nonceCache.get(Nonce.SignUpNonce(token))
      _ <- userId match
        case Some(userId) => recruiterModel.verify(userId)
        case _            => ZIO.fail(ErrorResponse.NotFound("invalid-token"))
    yield "Success! You may now login."
  }.mapError {
    case e: ErrorResponse => e
    case _                => ErrorResponse.InternalServerError
  }

  private def login(
      request: LoginRequest
  ): IO[ErrorResponse, SuccessResponse[JwtResponse]] = {
    for
      userOption <- recruiterModel.login(
        email = request.email,
        password = request.password
      )
      user <- userOption match
        case Some(user) if user.isVerified => ZIO.succeed(user)
        case Some(_) => ZIO.fail(ErrorResponse.Unauthorized("endpoint"))
        case None    => ZIO.fail(ErrorResponse.NotFound("no-account"))

      // Create a new JWT token.
      jwt <- JWTModel.encode(user.id)
    yield SuccessResponse(JwtResponse(jwt))
  }.mapError {
    case e: ErrorResponse => e
    case _                => ErrorResponse.InternalServerError
  }

  private def reset(
      request: ResetPasswordRequest
  ): IO[ErrorResponse, SuccessResponse[Boolean]] = {
    val resetUUID = UUID.randomUUID().toString
    for
      // If the user already exists and isn't verified, send a new verification email.
      userOption <- recruiterModel.userExists(email = request.email)
      response <- userOption match
        case Some(user) if !user.isVerified =>
          for
            _ <- nonceCache.put(Nonce.SignUpNonce(resetUUID), user.id)
            _ <- emailModel.sendPasswordResetEmail(request.email, resetUUID)
          yield SuccessResponse(true)
        case Some(_) => ZIO.fail(ErrorResponse.Unauthorized("endpoint"))
        case None    => ZIO.fail(ErrorResponse.NotFound("no-account"))
    yield response
  }.mapError {
    case e: ErrorResponse => e
    case _                => ErrorResponse.InternalServerError
  }

  val signUpHandler =
    signUpRoute.zServerLogic[Any](join)

  val resendHandler =
    resendVerificationEmailRoute.zServerLogic[Any](resend)

  val verifyHandler =
    verifyEmailRoute.zServerLogic[Any](verify)

  val loginHandler =
    loginRoute.zServerLogic[Any](login)

  val resetPasswordHandler =
    resetPasswordRoute.zServerLogic[Any](reset)

  val services = List(
    signUpHandler,
    resendHandler,
    verifyHandler,
    loginHandler,
    resetPasswordHandler
  ).map(ArmeriaZioServerInterpreter().toService)
end RecruitController

object RecruitController:
  val live =
    (EmailModel.resend ++ RecruiterEmailModel.generalLayer ++ IntegrationPostgresModel.live ++ RecruiterPostgresModel.postgres) >>> ZLayer {
      for
        emailModel <- ZIO.service[RecruiterEmailModel]
        integrationModel <- ZIO
          .service[IntegrationPostgresModel[Any, SQLException]]
        recruiterModel <- ZIO.service[RecruiterPostgresModel[Any, SQLException]]
        nonceCache <- ExpiringCache.make[Nonce, Nonce.RecruiterId](12.hours)
        s3Model <- ZIO.service[S3Model]
        fileConnector <- ZIO.service[FileConnector]
      yield new RecruitController(
        emailModel,
        recruiterModel,
        integrationModel,
        nonceCache,
        s3Model,
        fileConnector
      )
    }
end RecruitController
