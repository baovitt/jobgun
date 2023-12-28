package com.jobgun.shared.backend
package profile.domain.requests

// STTP Imports:
import sttp.tapir.{Schema, TapirFile}
import sttp.model.Part

final case class UploadResumeRequest(
    file: Part[TapirFile]
) derives Schema
