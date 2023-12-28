package com.jobgun.shared.backend
package profile.domain.requests

// STTP Imports:
import sttp.tapir.{Schema, TapirFile}
import sttp.model.Part

final case class SignUpRequest(
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    resume: Part[TapirFile],
    fileExtension: String
) derives Schema
