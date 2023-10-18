package com.jobgun.ui.domain.requests

import sttp.tapir.generic.auto.{*, given}
import org.scalajs.dom.File

import com.jobgun.shared.domain.requests.JobRequest

final case class JobSearchWithResumeRequest(
    file: File
) extends JobRequest

object JobSearchWithResumeRequest:
  import sttp.tapir.Schema

  given Schema[JobSearchWithResumeRequest] = Schema.derived
end JobSearchWithResumeRequest
