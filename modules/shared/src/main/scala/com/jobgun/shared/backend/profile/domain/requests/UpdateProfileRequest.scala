package com.jobgun.shared.backend.profile
package domain.requests

import zio.json.JsonCodec
import sttp.tapir.Schema

import com.jobgun.shared.backend.profile.domain.Profile

final case class UpdateProfileRequest(
    jwt: String,
    newProfile: Profile
) derives Schema,
      JsonCodec