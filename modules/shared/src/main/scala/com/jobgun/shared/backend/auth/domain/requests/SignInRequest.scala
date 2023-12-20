package com.jobgun.shared.backend.auth
package domain.requests

import sttp.tapir.Schema
import zio.json.JsonCodec

final case class SignInRequest(email: String, password: String)
    derives Schema,
      JsonCodec
