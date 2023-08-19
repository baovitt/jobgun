package com.jobgun.domain.requests

final case class EmbedUserRequest private (resume: String)

object EmbedUserRequest:
  import sttp.tapir.Schema
  import zio.json.{
    JsonDecoder,
    DeriveJsonDecoder,
    JsonEncoder,
    DeriveJsonEncoder
  }

  given Schema[EmbedUserRequest] = Schema.derived

  given JsonDecoder[EmbedUserRequest] = DeriveJsonDecoder.gen
  given JsonEncoder[EmbedUserRequest] = DeriveJsonEncoder.gen
end EmbedUserRequest
