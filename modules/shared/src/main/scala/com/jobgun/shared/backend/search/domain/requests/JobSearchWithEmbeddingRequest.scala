package com.jobgun.shared.backend
package search.domain.requests

// STTP Imports:
import sttp.tapir.Schema

// ZIO Imports:
import zio.json.JsonCodec

final case class JobSearchWithEmbeddingRequest(
    page: Int,
    embedding: List[Double],
    filters: search.domain.SearchFilter
) derives Schema,
      JsonCodec
