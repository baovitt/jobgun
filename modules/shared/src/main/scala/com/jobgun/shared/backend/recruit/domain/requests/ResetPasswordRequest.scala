package com.jobgun.shared.backend
package recruit.domain.requests

// STTP Imports:
import sttp.tapir.Schema

// ZIO Imports:
import zio.json.JsonCodec

final case class ResetPasswordRequest(email: String) derives Schema, JsonCodec
