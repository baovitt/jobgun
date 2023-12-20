package com.jobgun.shared.backend.profile
package domain.requests

import sttp.tapir.Schema
import sttp.model.Part
import java.io.File

final case class UploadResumeRequest(
    jwt: String,
    file: Part[File]
) derives Schema