package com.jobgun.domain.responses

import com.jobgun.shared.domain.ResponseJobListing

final case class JobSearchFromEmbeddingResponse private (
    listings: List[ResponseJobListing]
)

object JobSearchFromEmbeddingResponse:
  import sttp.tapir.Schema
  import zio.json.{
    JsonDecoder,
    DeriveJsonDecoder,
    JsonEncoder,
    DeriveJsonEncoder
  }

  given Schema[JobSearchFromEmbeddingResponse] = Schema.derived

  given JsonDecoder[JobSearchFromEmbeddingResponse] = DeriveJsonDecoder.gen
  given JsonEncoder[JobSearchFromEmbeddingResponse] = DeriveJsonEncoder.gen

  def apply(
      jobs: zio.Chunk[ResponseJobListing]
  ): JobSearchFromEmbeddingResponse =
    new JobSearchFromEmbeddingResponse(jobs.toList)
end JobSearchFromEmbeddingResponse
