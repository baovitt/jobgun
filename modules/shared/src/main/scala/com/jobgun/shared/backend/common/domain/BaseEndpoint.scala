package com.jobgun.shared.backend.common.domain

// STTP Imports:
import sttp.model.StatusCode
import sttp.tapir.json.zio.jsonBody
import sttp.tapir.ztapir.{
  endpoint,
  oneOf,
  oneOfDefaultVariant,
  oneOfVariant,
  statusCode,
  emptyOutputAs
}

final val baseEndpoint =
  endpoint
    .errorOut(
      oneOf[ErrorResponse](
        oneOfVariant(
          statusCode(StatusCode.BadRequest)
            .and(jsonBody[ErrorResponse.BadRequest])
        ),
        oneOfVariant(
          statusCode(StatusCode.NotFound)
            .and(jsonBody[ErrorResponse.NotFound])
        ),
        oneOfVariant(
          statusCode(StatusCode.Unauthorized)
            .and(jsonBody[ErrorResponse.Unauthorized])
        ),
        oneOfVariant(
          statusCode(StatusCode.NoContent)
            .and(emptyOutputAs(ErrorResponse.NoContent))
        ),
        oneOfDefaultVariant(
          statusCode(StatusCode.InternalServerError)
            .and(emptyOutputAs(ErrorResponse.InternalServerError))
        )
      )
    )
