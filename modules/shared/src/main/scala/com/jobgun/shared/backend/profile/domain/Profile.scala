package com.jobgun.shared.backend
package profile.domain

// STTP Imports:
import sttp.tapir.Schema

// ZIO Imports:
import zio.json.JsonCodec

final case class Profile(
    firstName: String,
    lastName: String
) derives Schema,
      JsonCodec
