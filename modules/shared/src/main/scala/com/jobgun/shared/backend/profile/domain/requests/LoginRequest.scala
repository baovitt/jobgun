package com.jobgun.shared.backend
package profile.domain.requests

// STTP Imports:
import sttp.tapir.Schema

// ZIO Imports:
import zio.json.JsonCodec

final case class LoginRequest(email: String, password: String)
    derives Schema,
      JsonCodec
