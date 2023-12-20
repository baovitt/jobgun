package com.jobgun.shared.backend.profile
package domain.responses

import zio.json.JsonCodec
import sttp.tapir.Schema

import com.jobgun.shared.backend.profile.domain.Profile

final case class UploadResumeResponse(
    newProfile: Profile
) derives Schema,
      JsonCodec
