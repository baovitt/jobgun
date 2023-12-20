package com.jobgun.controller

// ZIO Imports:
import zio.*
import zio.json.*

// Jobgun Imports:
import com.jobgun.shared.backend.auth.domain.requests.*
import com.jobgun.shared.backend.auth.domain.responses.*
import com.jobgun.shared.backend.auth.domain.sql.*
import com.jobgun.shared.backend.auth.domain.AuthRoutes.*
import com.jobgun.shared.backend.auth.domain.*
import com.jobgun.shared.backend.auth.model.PostgresModel
import com.jobgun.shared.backend.model.EmailModel
import com.jobgun.shared.backend.utils.ExpiringCache
import com.jobgun.shared.backend.model.auth.JWTModel

// STTP Imports:
import sttp.model.StatusCode
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.ztapir.RichZEndpoint
import sttp.tapir.server.armeria.zio.ArmeriaZioServerInterpreter

// JAVA Imports:
import java.sql.SQLException
import java.util.UUID

final class AuthController(
    emailModel: EmailModel,
    postgresModel: PostgresModel[Any, SQLException],
    nonceCache: ExpiringCache[Nonce, Nonce.UserId]
):

  given zio.Runtime[Any] = zio.Runtime.default

  private def join(
      request: SignUpRequest
  ): IO[ErrorResponse, SignUpResponse] = {
    def sendVerificationEmail(id: Int) =
      val verificationUUID = UUID.randomUUID().toString
      for
        _ <- nonceCache.put(Nonce.SignUpNonce(verificationUUID), id)
        _ <- emailModel.sendVerificationEmail(request.email, verificationUUID)
      yield SignUpResponse(success = true)

    for
      // If the user already exists and isn't verified, send a new verification email.
      userOption <- postgresModel.userExists(email = request.email)
      response <- userOption match
        case Some(user) if user.isVerified =>
          ZIO.fail(ErrorResponse.Unauthorized("endpoint"))
        case Some(user) => sendVerificationEmail(user.id)
        case None =>
          for
            userId <- postgresModel.create(
              UserAccountTable(
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                password = request.password,
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
  ): IO[ErrorResponse, ResendVerificationEmailResponse] = {
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
          yield ResendVerificationEmailResponse(success = true)
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
      request: SignInRequest
  ): IO[ErrorResponse, SignInResponse] = {
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
    yield SignInResponse.SignInSuccess(jwt = jwt)
  }.mapError {
    case e: ErrorResponse => e
    case _                => ErrorResponse.InternalServerError
  }

  private def reset(
      request: ResetPasswordRequest
  ): IO[ErrorResponse, ResetPasswordResponse] = {
    val resetUUID = UUID.randomUUID().toString
    for
      // If the user already exists and isn't verified, send a new verification email.
      userOption <- postgresModel.userExists(email = request.email)
      response <- userOption match
        case Some(user) if !user.isVerified =>
          for
            _ <- nonceCache.put(Nonce.SignUpNonce(resetUUID), user.id)
            _ <- emailModel.sendPasswordResetEmail(request.email, resetUUID)
          yield ResetPasswordResponse(success = true)
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

  val signInHandler =
    signInRoute.zServerLogic[Any](login)

  val resetPasswordHandler =
    resetPasswordRoute.zServerLogic[Any](reset)

  val services = List(
    signUpHandler,
    resendHandler,
    verifyHandler,
    signInHandler,
    resetPasswordHandler
  ).map(ArmeriaZioServerInterpreter().toService)
end AuthController

object AuthController:
  val live = (EmailModel.resend ++ PostgresModel.postgres) >>> ZLayer {
    for
      emailModel <- ZIO.service[EmailModel]
      postgresModel <- ZIO.service[PostgresModel[Any, SQLException]]
      nonceCache <- ExpiringCache.make[Nonce, Nonce.UserId](12.hours)
    yield new AuthController(emailModel, postgresModel, nonceCache)
  }
end AuthController
