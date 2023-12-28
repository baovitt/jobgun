package com.jobgun
package controller

// ZIO Imports:
import zio.*, zio.json.*

// Jobgun Imports:
import shared.backend as Backend

import Backend.profile.model.{PostgresModel, ProfileEmailModel}
import Backend.profile.domain.{requests as profileRequests, *}
import profileRequests.*, ProfileRoutes.*, sql.UserAccountTable

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

final class AuthController(
    emailModel: ProfileEmailModel,
    postgresModel: PostgresModel[Any, SQLException],
    nonceCache: ExpiringCache[Nonce, Nonce.UserId],
    s3Model: S3Model
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
      userOption <- postgresModel.userExists(email = request.email)
      response <- userOption match
        case Some(user) if user.isVerified =>
          ZIO.fail(ErrorResponse.Unauthorized("endpoint"))
        case Some(user) => sendVerificationEmail(user.id)
        case None =>
          for
            fileLocation <- S3Model.FileLocation.applyZIO(
              S3Model.resumesBucket,
              request.fileExtension,
              S3Model.S3Folder.ProfileFolder
            )
            _ <- s3Model.putFile(request.resume.body, fileLocation)
            userId <- postgresModel.create(
              UserAccountTable(
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                password = request.password,
                resumeUrl =
                  s"https://jobgun-resumes.nyc3.cdn.digitaloceanspaces.com/profile/${fileLocation.fileName}.${fileLocation.fileExtension}",
                isVerified = false
              )
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
      userOption <- postgresModel.userExists(email = request.email)
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
        case Some(userId) => postgresModel.verify(userId)
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
      userOption <- postgresModel.login(
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
      userOption <- postgresModel.userExists(email = request.email)
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
end AuthController

object AuthController:
  val live =
    (EmailModel.resend ++ ProfileEmailModel.generalLayer ++ PostgresModel.postgres) >>> ZLayer {
      for
        emailModel <- ZIO.service[ProfileEmailModel]
        postgresModel <- ZIO.service[PostgresModel[Any, SQLException]]
        nonceCache <- ExpiringCache.make[Nonce, Nonce.UserId](12.hours)
        s3Model <- ZIO.service[S3Model]
      yield new AuthController(emailModel, postgresModel, nonceCache, s3Model)
    }
end AuthController
