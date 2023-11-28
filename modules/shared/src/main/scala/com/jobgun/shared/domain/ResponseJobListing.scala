package com.jobgun.shared.domain

import zio.json.{jsonField, JsonCodec}
import sttp.tapir.Schema

// Job listing for search results
final case class ResponseJobListing(
    primitive: ATSJobListing,
    overview: String,
    highlights: zio.Chunk[String],
    distance: Float
) derives Schema,
      JsonCodec
