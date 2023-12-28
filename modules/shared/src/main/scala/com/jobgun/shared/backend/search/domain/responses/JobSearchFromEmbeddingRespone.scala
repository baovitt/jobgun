package com.jobgun.shared.backend
package search.domain.responses

// STTP Imports:
import sttp.tapir.Schema

// ZIO Imports:
import zio.json.JsonCodec

// Jobgun Imports:
import search.domain.ATSJobListing

final case class JobSearchFromEmbeddingResponse(
    listings: List[ATSJobListing]
) derives Schema,
      JsonCodec