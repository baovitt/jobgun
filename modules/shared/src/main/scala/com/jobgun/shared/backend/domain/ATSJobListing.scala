package com.jobgun.shared.backend

package domain

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
    @jsonField("company_name") companyName: String,
    @jsonField("company_url") companyUrl: String,
    country: String,
    @jsonField("_additional") additional: Additional
) derives Schema,
      JsonCodec

final case class Additional private (
    distance: Double
) derives Schema,
      JsonCodec
