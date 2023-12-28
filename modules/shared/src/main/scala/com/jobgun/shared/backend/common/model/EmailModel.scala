package com.jobgun.shared.backend
package common.model

// ZIO Imports:
import zio.*

// Jobgun Imports:
import common.config.ResendConfig

trait EmailModel:
  def sendEmail(email: String, subject: String, html: String): Task[Unit]

  // def sendRecruiterVerificationEmail(email: String, token: String): Task[Unit] =
  //   sendEmail(
  //     email,
  //     subject = "Verify Your Email | JOBGUN.AI 🏴",
  //     html =
  //       s"<a href='https://api.jobgun.ai/v1/recruit/verify?token=$token'>Verify your email.</a> This link will expire after 12 hours."
  //   )
end EmailModel

object EmailModel:
  // Circe Imports:
  import io.circe.generic.auto.*
  import io.circe.syntax.*

  // STTP Imports:
  import sttp.client3.*
  import sttp.client3.circe.*
  import sttp.client3.httpclient.zio.{HttpClientZioBackend, send}

  final case class EmailSuccessResponse(token: String)
  final case class EmailSendRequest(
      from: String,
      to: String,
      subject: String,
      html: String
  )

  val resend = ZLayer {
    for config <- ZIO.service[ResendConfig]
    yield new EmailModel:

      def sendEmail(
          email: String,
          subject: String,
          html: String
      ): Task[Unit] = {
        val request = basicRequest
          .post(uri"https://api.resend.com/emails")
          .contentType("application/json")
          .auth
          .bearer(config.bearer)
          .response(asJson[EmailSuccessResponse])
          .body(
            EmailSendRequest(
              from = "jobgun.ai@blackflag.dev",
              to = email,
              subject = subject,
              html = html
            ).asJson
          )

        for
          response <- send(request)
          result <- ZIO.cond(
            response.code == sttp.model.StatusCode.Ok,
            (),
            new Exception(
              s"Failed to send verification email: ${response.body}"
            )
          )
        yield ()
      }.provideLayer(HttpClientZioBackend.layer())
  }
end EmailModel
