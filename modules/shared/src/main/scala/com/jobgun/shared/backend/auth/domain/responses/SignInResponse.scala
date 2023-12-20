package com.jobgun.shared.backend.auth
package domain.responses

import sttp.tapir.Schema
import zio.json.JsonCodec

sealed trait SignInResponse derives Schema, JsonCodec

object SignInResponse:
  final case class SignInSuccess(jwt: String) extends SignInResponse
      derives Schema,
        JsonCodec

  final case class SignInFailure(message: String) extends SignInResponse
      derives Schema,
        JsonCodec
end SignInResponse
