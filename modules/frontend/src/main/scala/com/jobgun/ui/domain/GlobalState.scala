package com.jobgun.ui.domain

import sttp.tapir.TapirFile
import com.jobgun.shared.domain.JobListing

final case class GlobalState(
    resume: Option[TapirFile],
    embedding: Option[List[Double]],
    jobListings: List[JobListing],
    resumeRequestInFlight: Boolean = false
)