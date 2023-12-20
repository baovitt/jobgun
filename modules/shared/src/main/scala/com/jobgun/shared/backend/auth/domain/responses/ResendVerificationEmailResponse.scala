package com.jobgun.shared.backend.auth
package domain.responses

import sttp.tapir.Schema
import zio.json.JsonCodec

final case class ResendVerificationEmailResponse(success: Boolean)
    derives Schema,
      JsonCodec
