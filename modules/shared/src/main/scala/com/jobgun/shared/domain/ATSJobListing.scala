package com.jobgun.shared.domain

import zio.json.{jsonField, JsonCodec}
import sttp.tapir.Schema

// Job listing retrieved from client ATS systems
final case class ATSJobListing(
    created: String,
    title: String,
    description: String,
    @jsonField("employment_type") employmentType: String,
    location: String,
    url: String,
    @jsonField("company_name") companyName: String
) derives Schema, JsonCodec
