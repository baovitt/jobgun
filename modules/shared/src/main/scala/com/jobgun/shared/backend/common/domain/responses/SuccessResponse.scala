package com.jobgun.shared.backend.common.domain.responses

// STTP Imports:
import sttp.tapir.Schema

// ZIO Imports:
import zio.json.JsonCodec

final case class SuccessResponse[R](
    response: R
) derives JsonCodec, Schema