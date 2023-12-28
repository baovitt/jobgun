package com.jobgun.shared.backend.common.domain.responses

// STTP Imports:
import sttp.tapir.Schema

// ZIO Imports:
import zio.json.JsonCodec

final case class JwtResponse(
    jwt: String
) derives JsonCodec, Schema