package com.jobgun.shared.backend
package profile.model

// ZIO Imports:
import zio.*

// Jobgun Imports:
import common.model.EmailModel

final case class ProfileEmailModel(emailModel: EmailModel):
  def sendVerificationEmail(email: String, token: String): Task[Unit] =
    emailModel.sendEmail(
      email,
      subject = "Verify Your Email | JOBGUN.AI 🏴",
      html =
        s"<a href='https://api.jobgun.ai/v1/verify?token=$token'>Verify your email.</a> This link will expire after 12 hours."
    )

  def sendPasswordResetEmail(email: String, token: String): Task[Unit] =
    emailModel.sendEmail(
      email,
      subject = "Reset Your Password | JOBGUN.AI 🏴",
      html =
        s"<a href='https://api.jobgun.ai/v1/reset?token=$token'>Reset your password.</a> This link will expire after 12 hours."
    )
end ProfileEmailModel

object ProfileEmailModel:
  val generalLayer: URLayer[EmailModel, ProfileEmailModel] = ZLayer {
    for emailModel <- ZIO.service[EmailModel]
    yield ProfileEmailModel(emailModel)
  }
end ProfileEmailModel
