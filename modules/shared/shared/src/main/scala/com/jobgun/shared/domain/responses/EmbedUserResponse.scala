package com.jobgun.shared.domain.responses

final case class EmbedUserResponse private (embedding: List[Double])

object EmbedUserResponse:
  import sttp.tapir.Schema
  import zio.json.{
    JsonDecoder,
    DeriveJsonDecoder,
    JsonEncoder,
    DeriveJsonEncoder
  }

  given Schema[EmbedUserResponse] = Schema.derived

  given JsonDecoder[EmbedUserResponse] = DeriveJsonDecoder.gen
  given JsonEncoder[EmbedUserResponse] = DeriveJsonEncoder.gen

  def fromEmbedding(embedding: zio.Chunk[Double]): EmbedUserResponse =
    new EmbedUserResponse(embedding.toList)
end EmbedUserResponse