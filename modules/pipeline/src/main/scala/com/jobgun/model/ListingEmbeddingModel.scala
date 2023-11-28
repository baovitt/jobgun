package com.jobgun.model

import com.jobgun.shared.model.EmbeddingModel

import zio.openai.Embeddings
import zio.openai.model.CreateEmbeddingRequest.Input
import zio.openai.model.OpenAIFailure
import zio.{Chunk, IO, ZIO, ZLayer}

trait ListingEmbeddingModel extends EmbeddingModel:
//   def embedText(text: String): IO[OpenAIFailure, Chunk[Double]]
end ListingEmbeddingModel

object ListingEmbeddingModel:
  val default = ZLayer {
    for embeddings <- ZIO.service[Embeddings]
    yield new ListingEmbeddingModel:
      def embedText(text: String): IO[OpenAIFailure, Chunk[Double]] =
        embeddings
          .createEmbedding(
            model = "text-embedding-ada-002",
            input = Input.String(text)
          )
          .map(_.data.flatMap(_.embedding))
  }

  def embedText(
      text: String
  ): ZIO[ListingEmbeddingModel, OpenAIFailure, Chunk[Double]] =
    ZIO.serviceWithZIO[ListingEmbeddingModel](_.embedText(text))
end ListingEmbeddingModel
