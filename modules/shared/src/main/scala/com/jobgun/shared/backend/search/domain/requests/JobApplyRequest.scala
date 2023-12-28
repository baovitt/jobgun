package com.jobgun.shared.backend
package search.domain.requests

// STTP Imports:
import sttp.tapir.Schema

// ZIO Imports:
import zio.json.JsonCodec

final case class JobApplyRequest(jobId: Int)
    derives Schema,
      JsonCodec