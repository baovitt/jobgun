package com.jobgun.shared.domain.requests

import sttp.tapir.generic.auto.{*, given}
import sttp.tapir.TapirFile
// import java.io.File

sealed trait JobRequest

object JobRequest: 

    final case class JobSearchWithEmbeddingRequest(
        page: Int,
        pageSize: Int,
        embedding: List[Double],
    ) extends JobRequest

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

    end JobSearchWithEmbeddingRequest

    final case class JobSearchWithResumeRequest(
        file: TapirFile
    ) extends JobRequest

    object JobSearchWithResumeRequest:
        import sttp.tapir.Schema

        given Schema[JobSearchWithResumeRequest] = Schema.derived
    end JobSearchWithResumeRequest

end JobRequest