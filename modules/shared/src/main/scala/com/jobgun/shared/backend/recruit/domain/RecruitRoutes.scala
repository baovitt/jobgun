package com.jobgun.shared.backend
package recruit.domain

object RecruitRoutes:

  // STTP Imports:
  import sttp.tapir.json.zio.jsonBody
  import sttp.tapir.ztapir.*
  import sttp.model.StatusCode

  // Jobgun Imports:
  import common.domain.responses.*
  import requests.*

  private val recruitEndpoint =
    common.domain.baseEndpoint.in("recruit")

  val signUpRoute =
    recruitEndpoint.post
      .in("join")
      .in(multipartBody[SignUpRequest])
      .out(jsonBody[SuccessResponse[Boolean]])
      .description("Registers a new recriter.")
      .name("Sign Up")

  val resendVerificationEmailRoute =
    recruitEndpoint.post
      .in("resend")
      .in(jsonBody[ResendVerificationEmailRequest])
      .out(jsonBody[SuccessResponse[Boolean]])
      .description("Resend the verification email for the recruiter.")
      .name("Resend Verification Email")

  val verifyEmailRoute =
    recruitEndpoint.patch
      .in("verify")
      .in(query[String]("token"))
      .out(stringBody)
      .description("Modifies the \"is verified\" field to true.")
      .name("Verify Email")

  val loginRoute =
    recruitEndpoint.post
      .in("login")
      .in(jsonBody[LoginRequest])
      .out(jsonBody[SuccessResponse[JwtResponse]])
      .description("Returns a JWT token given a user's credentials.")
      .name("Sign In")

  val resetPasswordRoute =
    recruitEndpoint.post
      .in("reset")
      .in(jsonBody[ResetPasswordRequest])
      .out(jsonBody[SuccessResponse[Boolean]])
      .description("Sends an email to reset the users password.")
      .name("Reset Password")

  val integrateRecruiteeRoute =
    recruitEndpoint.post
      .in("integrate")
      .in("recruitee")
      .in(jsonBody[IntegrateRecruiteeRequest])
      .securityIn(auth.bearer[String]())
      .out(jsonBody[SuccessResponse[Boolean]])
      .description("Adds the recruitee integration to the recruiter account.")
      .name("Recruitee Integration")

  val removeRecruiteeIntegrationRoute =
    recruitEndpoint.delete
      .in("integrate")
      .in("recruitee")
      .securityIn(auth.bearer[String]())
      .out(jsonBody[SuccessResponse[Boolean]])
      .description(
        "Removes the recruitee integration from the recruiter account."
      )
      .name("Remove Recruitee Integration")

  val endpoints = {
    val endpoints = List(
      signUpRoute,
      resendVerificationEmailRoute,
      verifyEmailRoute,
      loginRoute,
      resetPasswordRoute,
      integrateRecruiteeRoute,
      removeRecruiteeIntegrationRoute
    )

    endpoints.map(_.tags(List("Recruit Endpoints")))
  }
end RecruitRoutes
