package com.jobgun.shared.backend
package search.domain.requests

// STTP Imports:
import sttp.tapir.{Schema, TapirFile}
import sttp.model.Part

final case class JobSearchWithResumeRequest(
    file: Part[TapirFile]
) derives Schema
