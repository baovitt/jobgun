package com.jobgun.shared.backend
package search.domain

// ZIO Imports:
import zio.json.{JsonDecoder, DeriveJsonDecoder, jsonField}
import zio.Chunk

// Jobgun Imports:
import com.jobgun.shared.backend.search.domain.ATSJobListing

final case class JobListings private (
    @jsonField("JobListing") jobListings: Chunk[ATSJobListing]
) derives JsonDecoder

final case class GetResponse private (@jsonField("Get") get: JobListings)
    derives JsonDecoder

final case class DataResponse private (data: GetResponse) derives JsonDecoder

final case class WeaviateSearchResponse private (result: DataResponse)
    derives JsonDecoder:
  def jobListings: Chunk[ATSJobListing] = result.data.get.jobListings
end WeaviateSearchResponse
