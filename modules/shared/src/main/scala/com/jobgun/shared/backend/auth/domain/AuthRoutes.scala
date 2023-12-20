package com.jobgun.shared.backend.auth
package domain

object AuthRoutes:

  // STTP Imports:
  import sttp.tapir.json.zio.jsonBody
  import sttp.tapir.ztapir.*
  import sttp.model.StatusCode

  // Jobgun Imports:
  import com.jobgun.shared.backend.auth.domain.requests.*
  import com.jobgun.shared.backend.auth.domain.responses.*

  private val baseEndpoint =
    endpoint
      .in("v1")
      .in("auth")
      .errorOut(
        oneOf[ErrorResponse](
          oneOfVariant(
            statusCode(StatusCode.NotFound)
              .and(jsonBody[ErrorResponse.NotFound])
          ),
          oneOfVariant(
            statusCode(StatusCode.Unauthorized)
              .and(jsonBody[ErrorResponse.Unauthorized])
          ),
          oneOfVariant(
            statusCode(StatusCode.NoContent)
              .and(emptyOutputAs(ErrorResponse.NoContent))
          ),
          oneOfDefaultVariant(
            statusCode(StatusCode.InternalServerError)
              .and(emptyOutputAs(ErrorResponse.InternalServerError))
          )
        )
      )

  val signUpRoute =
    baseEndpoint.post
      .in("join")
      .in(jsonBody[SignUpRequest])
      .out(jsonBody[SignUpResponse])
      .description("Creates a new user.")
      .name("Sign Up")

  val resendVerificationEmailRoute =
    baseEndpoint.post
      .in("resend")
      .in(jsonBody[ResendVerificationEmailRequest])
      .out(jsonBody[ResendVerificationEmailResponse])
      .description("Resend the verification email for a user.")
      .name("Resend Verification Email")

  val verifyEmailRoute =
    baseEndpoint.patch
      .in("verify")
      .in(query[String]("token"))
      .out(stringBody)
      .description("Modifies the \"is verified\" field to true.")
      .name("Verify Email")

  val signInRoute =
    baseEndpoint.post
      .in("login")
      .in(jsonBody[SignInRequest])
      .out(jsonBody[SignInResponse])
      .description("Returns a JWT token given a user's credentials.")
      .name("Sign In")

  val resetPasswordRoute =
    baseEndpoint.post
      .in("reset")
      .in(jsonBody[ResetPasswordRequest])
      .out(jsonBody[ResetPasswordResponse])
      .description("Sends an email to reset the users password.")
      .name("Reset Password")

  val endpoints = {
    val endpoints = List(
      signUpRoute,
      resendVerificationEmailRoute,
      verifyEmailRoute,
      signInRoute,
      resetPasswordRoute
    )

    endpoints.map(_.tags(List("Auth Endpoints")))
  }
end AuthRoutes
