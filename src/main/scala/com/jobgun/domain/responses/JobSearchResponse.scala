package com.jobgun.domain.responses

import com.jobgun.domain.JobListing

final case class JobSearchResponse private (listings: List[JobListing])

object JobSearchResponse:
  import sttp.tapir.Schema
  import zio.json.{
    JsonDecoder,
    DeriveJsonDecoder,
    JsonEncoder,
    DeriveJsonEncoder
  }

  given Schema[JobSearchResponse] = Schema.derived

  given JsonDecoder[JobSearchResponse] = DeriveJsonDecoder.gen
  given JsonEncoder[JobSearchResponse] = DeriveJsonEncoder.gen

  def fromJobs(jobs: zio.Chunk[JobListing]): JobSearchResponse =
    new JobSearchResponse(jobs.toList)
end JobSearchResponse
