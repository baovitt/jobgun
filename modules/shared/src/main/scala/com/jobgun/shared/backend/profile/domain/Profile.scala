package com.jobgun.shared.backend.profile
package domain

import zio.json.JsonCodec
import sttp.tapir.Schema

final case class Profile(
    firstName: String,
    lastName: String,
) derives Schema, JsonCodec
