package com.jobgun.shared.backend

package domain.responses

import com.jobgun.shared.backend.domain.ATSJobListing

import zio.json.JsonCodec
import sttp.tapir.Schema

final case class JobSearchFromEmbeddingResponse(
    listings: List[ATSJobListing]
) derives Schema,
      JsonCodec
