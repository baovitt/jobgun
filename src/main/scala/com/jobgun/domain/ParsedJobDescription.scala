package com.jobgun.domain

import zio.Chunk
import zio.json.jsonField

final case class ParsedJobDescription private (
    @jsonField("required/preferred certifications") certifications: Chunk[
      String
    ],
    @jsonField("required/preferred education") education: Chunk[String],
    @jsonField("required/preferred experience") experience: Chunk[String],
    @jsonField("required/preferred licenses") licenses: Chunk[String],
    @jsonField(
      "required/preferred security credentials"
    ) securityCredentials: Chunk[String],
    @jsonField("required/preferred misc requirements") misc: Chunk[String]
)

object ParsedJobDescription:
  import zio.json.{
    JsonDecoder,
    JsonEncoder,
    DeriveJsonDecoder,
    DeriveJsonEncoder
  }

  given JsonDecoder[ParsedJobDescription] = DeriveJsonDecoder.gen
  given JsonEncoder[ParsedJobDescription] = DeriveJsonEncoder.gen
end ParsedJobDescription
