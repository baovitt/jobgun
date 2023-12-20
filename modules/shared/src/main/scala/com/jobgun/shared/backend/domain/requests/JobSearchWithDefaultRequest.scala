package com.jobgun.shared.backend

package domain.requests

import sttp.tapir.Schema
import zio.json.JsonCodec

final case class JobSearchWithDefaultRequest(default: String)
    derives Schema,
      JsonCodec
