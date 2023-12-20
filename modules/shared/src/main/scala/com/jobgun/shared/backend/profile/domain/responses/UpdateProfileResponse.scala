package com.jobgun.shared.backend.profile
package domain.responses

import zio.json.JsonCodec
import sttp.tapir.Schema

final case class UploadProfileResponse(
    success: Boolean
) derives Schema,
      JsonCodec
