package com.jobgun.utils.extensions

import com.jobgun.shared.domain.{QueryableListing, ResponseJobListing}
import com.jobgun.domain.SuperParserResponse
import zio.Chunk

object QueryableListingExtensions:

  extension (listings: Chunk[QueryableListing])
    def search(user: SuperParserResponse): Chunk[ResponseJobListing] =
      ???

end QueryableListingExtensions
