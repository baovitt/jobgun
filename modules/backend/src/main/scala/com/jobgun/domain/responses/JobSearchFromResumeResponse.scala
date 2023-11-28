package com.jobgun.domain.responses

import com.jobgun.shared.domain.ResponseJobListing

final case class JobSearchFromResumeResponse private (
    listings: List[ResponseJobListing],
    embedding: List[Double]
)

object JobSearchFromResumeResponse:
  import sttp.tapir.Schema
  import zio.json.{
    JsonDecoder,
    DeriveJsonDecoder,
    JsonEncoder,
    DeriveJsonEncoder
  }

  given Schema[JobSearchFromResumeResponse] = Schema.derived

  given JsonDecoder[JobSearchFromResumeResponse] = DeriveJsonDecoder.gen
  given JsonEncoder[JobSearchFromResumeResponse] = DeriveJsonEncoder.gen

  def apply(
      jobs: zio.Chunk[ResponseJobListing],
      embedding: zio.Chunk[Double]
  ): JobSearchFromResumeResponse =
    new JobSearchFromResumeResponse(jobs.toList, embedding.toList)
end JobSearchFromResumeResponse
