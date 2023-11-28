package com.jobgun.domain.requests

import sttp.tapir.generic.auto.{*, given}
import sttp.model.Part
import java.io.File

final case class JobSearchWithResumeRequest(
    file: Part[File]
)

object JobSearchWithResumeRequest:
  import sttp.tapir.Schema

  given Schema[JobSearchWithResumeRequest] = Schema.derived
end JobSearchWithResumeRequest
