package com.jobgun.ui.domain

import org.scalajs.dom.File
import com.jobgun.shared.domain.JobListing

final case class GlobalState(
    resume: Option[File],
    embedding: Option[List[Double]],
    jobListings: List[JobListing],
    resumeRequestInFlight: Boolean = false
    // seeJobs: Boolean = false
)
