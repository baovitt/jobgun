package com.jobgun.shared.domain.requests

final case class JobSearchRequest(
    page: Int,
    pageSize: Int,
    embedding: List[Double]
)

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
