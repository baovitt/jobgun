package com.jobgun.shared.backend
package search.domain

// STTP Imports:
import sttp.tapir.Schema

// ZIO Imports:
import zio.json.JsonCodec

final case class SearchFilter(
    jobType: SearchFilter.JobTypeFilter,
    preferredTitle: String
) derives Schema,
      JsonCodec

object SearchFilter:
  final case class JobTypeFilter(
      fullTime: Boolean,
      partTime: Boolean,
      contract: Boolean,
      internship: Boolean,
      other: Boolean
  ) derives Schema,
        JsonCodec
end SearchFilter
