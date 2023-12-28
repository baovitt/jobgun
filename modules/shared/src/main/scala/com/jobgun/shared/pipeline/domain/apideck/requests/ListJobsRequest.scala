package com.jobgun.shared.pipeline
package domain.apideck.requests

import sttp.tapir.Schema
import zio.json.JsonCodec

final case class JobSearchWithDefaultRequest(default: String)
    derives Schema,
      JsonCodec
