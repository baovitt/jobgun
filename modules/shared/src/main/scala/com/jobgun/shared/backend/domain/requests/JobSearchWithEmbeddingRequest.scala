package com.jobgun.shared.backend

package domain.requests

import sttp.tapir.Schema
import zio.json.JsonCodec

final case class JobSearchWithEmbeddingRequest(
    page: Int,
    embedding: List[Double],
    filters: JobSearchWithEmbeddingRequest.JobSearchFilters
) derives Schema,
      JsonCodec

object JobSearchWithEmbeddingRequest:
  final case class JobType(
      fullTime: Boolean,
      partTime: Boolean,
      contract: Boolean,
      internship: Boolean,
      other: Boolean
  ) derives Schema,
        JsonCodec

  final case class JobSearchFilters(
      jobType: JobType,
      preferredTitle: String
  ) derives Schema,
        JsonCodec
end JobSearchWithEmbeddingRequest
