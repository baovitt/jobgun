package com.jobgun.shared.backend
package recruit.domain.requests

import sttp.tapir.{Schema, TapirFile}
import sttp.model.Part

final case class SignUpRequest(
    firstName: String,
    lastName: String,
    company: String,
    companyUrl: String,
    email: String,
    password: String,
    resume: Part[TapirFile],
    fileExtension: String
) derives Schema
