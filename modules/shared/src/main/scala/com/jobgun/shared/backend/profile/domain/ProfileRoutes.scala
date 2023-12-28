package com.jobgun.shared.backend
package profile.domain

object ProfileRoutes:

  // STTP Imports:
  import sttp.tapir.json.zio.jsonBody
  import sttp.tapir.ztapir.*
  import sttp.model.StatusCode

  // Jobgun Imports:
  import common.domain.responses.*
  import requests.*

  private val profileEndpoint = 
    common.domain.baseEndpoint.in("profile")

  val signUpRoute =
    profileEndpoint.post
      .in("join")
      .in(multipartBody[SignUpRequest])
      .out(jsonBody[SuccessResponse[Boolean]])
      .description("Creates a new user.")
      .name("Sign Up")

  val resendVerificationEmailRoute =
    profileEndpoint.post
      .in("resend")
      .in(jsonBody[ResendVerificationEmailRequest])
      .out(jsonBody[SuccessResponse[Boolean]])
      .description("Resend the verification email for a user.")
      .name("Resend Verification Email")

  val verifyEmailRoute =
    profileEndpoint.patch
      .in("verify")
      .in(query[String]("token"))
      .out(stringBody)
      .description("Modifies the \"is verified\" field to true.")
      .name("Verify Email")

  val loginRoute =
    profileEndpoint.post
      .in("login")
      .in(jsonBody[LoginRequest])
      .out(jsonBody[SuccessResponse[JwtResponse]])
      .description("Returns a JWT token given a user's credentials.")
      .name("Login")

  val resetPasswordRoute =
    profileEndpoint.post
      .in("reset")
      .in(jsonBody[ResetPasswordRequest])
      .out(jsonBody[SuccessResponse[Boolean]])
      .description("Sends an email to reset the users password.")
      .name("Reset Password")

  val deleteAccountRoute =
    profileEndpoint.delete
      .in("delete")
      .securityIn(auth.bearer[String]())
      .out(jsonBody[SuccessResponse[Boolean]])
      .description("Deletes a user's account.")
      .name("Delete Account")

  val updateProfileRoute =
    profileEndpoint.post
      .in("update")
      .in(jsonBody[Profile])
      .securityIn(auth.bearer[String]())
      .out(jsonBody[SuccessResponse[Boolean]])
      .description("Updates a user's account.")
      .name("Update Account")

  val uploadResumeProfileRoute =
    profileEndpoint.post
      .in("resume")
      .in(multipartBody[UploadResumeRequest])
      .securityIn(auth.bearer[String]())
      .out(jsonBody[SuccessResponse[Boolean]])
      .description("Updates or adds a user's resume.")
      .name("Upload Resume")

  val endpoints = {
    val endpoints = List(
      signUpRoute,
      resendVerificationEmailRoute,
      verifyEmailRoute,
      loginRoute,
      resetPasswordRoute
    )

    endpoints.map(_.tags(List("Profile Endpoints")))
  }
end ProfileRoutes
