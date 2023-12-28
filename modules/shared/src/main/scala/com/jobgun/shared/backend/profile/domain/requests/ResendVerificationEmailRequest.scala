package com.jobgun.shared.backend
package profile.domain.requests

// STTP Imports:
import sttp.tapir.Schema

// ZIO Imports:
import zio.json.JsonCodec

final case class ResendVerificationEmailRequest(email: String)
    derives Schema,
      JsonCodec
