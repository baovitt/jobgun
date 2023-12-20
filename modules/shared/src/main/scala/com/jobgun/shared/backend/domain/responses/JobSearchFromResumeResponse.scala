package com.jobgun.shared.backend

package domain.responses

import com.jobgun.shared.backend.domain.ATSJobListing

import zio.json.JsonCodec
import sttp.tapir.Schema

final case class JobSearchFromResumeResponse(
    listings: List[ATSJobListing],
    embedding: List[Double]
) derives Schema,
      JsonCodec
