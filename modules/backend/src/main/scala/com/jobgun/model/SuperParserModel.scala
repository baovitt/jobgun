package com.jobgun.model

import com.jobgun.domain.SuperParserResponse

// Tapir Imports:
import sttp.tapir.{TapirFile, Schema}
import sttp.client3.*
import sttp.client3.httpclient.zio.*
import sttp.client3.circe.*
import io.circe.generic.auto.*

import zio.*

object SuperParserModel:
  def superParserRequest(
      file: TapirFile
  ): Task[SuperParserResponse] = {
    val request = basicRequest
      .post(uri"https://api.superparser.com/parse")
      .header("X-API-Key", "a1C6N3Lsg56fUckZRHkXZ93KycL2lToX5kFfXTsj")
      .multipartBody(multipartFile("file_name", file))
      .response(asJson[SuperParserResponse])

    for
      result <- send(request)
      response <- result.body match
        case Left(e)     => ZIO.fail(Exception(e.toString))
        case Right(body) => ZIO.succeed(body)
    yield response
  }.provideLayer(HttpClientZioBackend.layer())
end SuperParserModel
