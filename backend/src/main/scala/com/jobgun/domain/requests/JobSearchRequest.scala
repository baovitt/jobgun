package com.jobgun.domain.requests

final case class JobSearchRequest(embedding: List[Double])

object JobSearchRequest:
  import sttp.tapir.Schema
  import zio.json.{
    JsonDecoder,
    DeriveJsonDecoder,
    JsonEncoder,
    DeriveJsonEncoder
  }

  given Schema[JobSearchRequest] = Schema.derived

  given JsonDecoder[JobSearchRequest] = DeriveJsonDecoder.gen
  given JsonEncoder[JobSearchRequest] = DeriveJsonEncoder.gen
end JobSearchRequest
