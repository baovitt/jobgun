package com.jobgun.shared.domain.responses

import com.jobgun.shared.domain.JobListing

sealed trait JobResponse

object JobResponse:

    final case class JobSearchFromResumeResponse private (
        listings: List[JobListing],
        embedding: List[Double]
    ) extends JobResponse

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

        def apply(jobs: zio.Chunk[JobListing], embedding: zio.Chunk[Double]): JobSearchFromResumeResponse =
            new JobSearchFromResumeResponse(jobs.toList, embedding.toList)
    end JobSearchFromResumeResponse

    final case class JobSearchFromEmbeddingResponse private (
        listings: List[JobListing]
    ) extends JobResponse

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

        def apply(jobs: zio.Chunk[JobListing]): JobSearchFromEmbeddingResponse =
            new JobSearchFromEmbeddingResponse(jobs.toList)
    end JobSearchFromEmbeddingResponse

end JobResponse