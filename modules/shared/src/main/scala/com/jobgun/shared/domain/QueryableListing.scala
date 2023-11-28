package com.jobgun.shared.domain

import zio.json.{jsonField, JsonCodec}

// Job listing for the database
final case class QueryableListing(
    listing: ATSJobListing,
    description: JobDescriptionWithEmbeddings
) derives JsonCodec:
  def toResponse(distance: Float): ResponseJobListing = ResponseJobListing(
    listing,
    description.summary,
    description.highlights,
    distance
  )
end QueryableListing
