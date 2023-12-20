package com.jobgun.shared.backend

package domain.requests

import sttp.tapir.Schema
import sttp.model.Part
import java.io.File

final case class JobSearchWithResumeRequest(
    file: Part[File]
) derives Schema
