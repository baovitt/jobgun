package com.jobgun.shared.domain

import zio.json.jsonField

final case class JobListing private (
    created: String,
    title: String,
    description: String,
    @jsonField("employment_type") employmentType: String,
    location: String,
    url: String,
    @jsonField("company_name") companyName: String,
)

object JobListing:
  import sttp.tapir.Schema
  import zio.json.{
    JsonDecoder,
    JsonEncoder,
    DeriveJsonDecoder,
    DeriveJsonEncoder
  }

  given Schema[JobListing] = Schema.derived

  given JsonDecoder[JobListing] = DeriveJsonDecoder.gen
  given JsonEncoder[JobListing] = DeriveJsonEncoder.gen
end JobListing
