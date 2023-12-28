package com.jobgun.shared.backend
package search.domain.responses

// STTP Imports:
import sttp.tapir.Schema

// ZIO Imports:
import zio.json.JsonCodec

// Jobgun Imports:
import search.domain.ATSJobListing

final case class JobSearchFromResumeResponse(
    listings: List[ATSJobListing],
    embedding: List[Double]
) derives Schema,
      JsonCodec
