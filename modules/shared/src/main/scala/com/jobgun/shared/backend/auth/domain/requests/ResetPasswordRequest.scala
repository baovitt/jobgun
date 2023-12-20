package com.jobgun.shared.backend.auth
package domain.requests

import sttp.tapir.Schema
import zio.json.JsonCodec

final case class ResetPasswordRequest(email: String) derives Schema, JsonCodec
