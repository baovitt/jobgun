package com.jobgun.domain.requests

final case class JobSearchWithDefaultRequest(default: String)

object JobSearchWithDefaultRequest:
  import sttp.tapir.Schema
  import zio.json.{
    JsonDecoder,
    DeriveJsonDecoder,
    JsonEncoder,
    DeriveJsonEncoder
  }

  given Schema[JobSearchWithDefaultRequest] = Schema.derived

  given JsonDecoder[JobSearchWithDefaultRequest] = DeriveJsonDecoder.gen
  given JsonEncoder[JobSearchWithDefaultRequest] = DeriveJsonEncoder.gen
end JobSearchWithDefaultRequest
