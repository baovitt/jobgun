package com.jobgun.shared.backend
package model

import zio.openai.Embeddings
import zio.openai.model.CreateEmbeddingRequest.Input
import zio.openai.model.OpenAIFailure
import zio.{Chunk, IO, ZIO, ZLayer}

trait EmbeddingModel:
  def embedText(text: String): IO[OpenAIFailure, Chunk[Double]]
end EmbeddingModel

object EmbeddingModel:
  val default = ZLayer {
    for embeddings <- ZIO.service[Embeddings]
    yield new EmbeddingModel:
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
  ): ZIO[EmbeddingModel, OpenAIFailure, Chunk[Double]] =
    ZIO.serviceWithZIO[EmbeddingModel](_.embedText(text))
end EmbeddingModel
