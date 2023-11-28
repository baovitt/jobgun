package com.jobgun.domain

// ZIO Imports:
import zio.json.{JsonDecoder, DeriveJsonDecoder, jsonField}
import zio.Chunk

// Jobgun Imports:
import com.jobgun.shared.domain.ResponseJobListing

final case class JobListings private (
    @jsonField("JobListing") jobListings: Chunk[ResponseJobListing]
)

object JobListings:
  given JsonDecoder[JobListings] = DeriveJsonDecoder.gen
end JobListings

final case class GetResponse private (@jsonField("Get") get: JobListings)

object GetResponse:
  given JsonDecoder[GetResponse] = DeriveJsonDecoder.gen
end GetResponse

final case class DataResponse private (data: GetResponse)

object DataResponse:
  given JsonDecoder[DataResponse] = DeriveJsonDecoder.gen
end DataResponse

final case class WeaviateSearchResponse private (result: DataResponse):
  def jobListings: Chunk[ResponseJobListing] = result.data.get.jobListings
end WeaviateSearchResponse

object WeaviateSearchResponse:
  given JsonDecoder[WeaviateSearchResponse] = DeriveJsonDecoder.gen
end WeaviateSearchResponse
