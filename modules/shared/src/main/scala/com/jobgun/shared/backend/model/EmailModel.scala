package com.jobgun.shared.backend
package model

import zio.*
import com.jobgun.shared.backend.config.ResendConfig

trait EmailModel:
  def sendEmail(email: String, subject: String, html: String): Task[Unit]

  def sendVerificationEmail(email: String, token: String): Task[Unit] =
    sendEmail(
      email,
      subject = "Verify Your Email | JOBGUN.AI 🏴",
      html =
        s"<a href='https://api.jobgun.ai/v1/verify?token=$token'>Verify your email.</a> This link will expire after 12 hours."
    )

  def sendPasswordResetEmail(email: String, token: String): Task[Unit] =
    sendEmail(
      email,
      subject = "Reset Your Password | JOBGUN.AI 🏴",
      html =
        s"<a href='https://api.jobgun.ai/v1/reset?token=$token'>Reset your password.</a> This link will expire after 12 hours."
    )
end EmailModel

object EmailModel:
  import io.circe.generic.auto._
  import io.circe.syntax._
  import sttp.client3._
  import sttp.client3.circe._
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
              from = "auth.jobgun.ai@blackflag.dev",
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
