package com.jobgun.domain.requests

final case class JobSearchWithEmbeddingRequest(
    page: Int,
    embedding: List[Double],
    filters: JobSearchWithEmbeddingRequest.JobSearchFilters
)

object JobSearchWithEmbeddingRequest:
  import sttp.tapir.Schema
  import zio.json.{
    JsonDecoder,
    DeriveJsonDecoder,
    JsonEncoder,
    DeriveJsonEncoder
  }

  given Schema[JobSearchWithEmbeddingRequest] = Schema.derived

  given JsonDecoder[JobSearchWithEmbeddingRequest] = DeriveJsonDecoder.gen
  given JsonEncoder[JobSearchWithEmbeddingRequest] = DeriveJsonEncoder.gen

  final case class JobType(
      fullTime: Boolean,
      partTime: Boolean,
      contract: Boolean,
      internship: Boolean,
      other: Boolean
  )

  object JobType:
    given Schema[JobType] = Schema.derived

    given JsonDecoder[JobType] = DeriveJsonDecoder.gen
    given JsonEncoder[JobType] = DeriveJsonEncoder.gen
  end JobType

  final case class JobSearchFilters(
      jobType: JobType,
      preferredTitle: String
  )

  object JobSearchFilters:
    given Schema[JobSearchFilters] = Schema.derived

    given JsonDecoder[JobSearchFilters] = DeriveJsonDecoder.gen
    given JsonEncoder[JobSearchFilters] = DeriveJsonEncoder.gen
  end JobSearchFilters
end JobSearchWithEmbeddingRequest
