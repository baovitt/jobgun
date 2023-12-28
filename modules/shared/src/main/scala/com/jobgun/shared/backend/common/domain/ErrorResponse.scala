package com.jobgun.shared.backend.common.domain

// STTP Imports:
import sttp.tapir.Schema

// ZIO Imports:
import zio.json.JsonCodec

sealed trait ErrorResponse derives Schema, JsonCodec

object ErrorResponse:
  final case class BadRequest(error: String) extends ErrorResponse
      derives Schema,
        JsonCodec

  final case class NotFound(what: String) extends ErrorResponse
      derives Schema,
        JsonCodec

  final case class Unauthorized(realm: String) extends ErrorResponse
      derives Schema,
        JsonCodec

  case object InternalServerError extends ErrorResponse
      derives Schema,
        JsonCodec

  case object NoContent extends ErrorResponse derives Schema, JsonCodec
end ErrorResponse
