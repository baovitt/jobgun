package com.jobgun.model

// ZIO Imports:
import zio.{Chunk, IO, ZIO, ZLayer}
import zio.json.DecoderOps

// Weaviate Imports:
import io.weaviate.client.base.Result
import io.weaviate.client.WeaviateClient
import io.weaviate.client.v1.graphql.query.argument.NearVectorArgument
import io.weaviate.client.v1.graphql.query.fields.Field
import io.weaviate.client.v1.graphql.model.GraphQLResponse

// Jobgun Imports:
import com.jobgun.domain.{WeaviateSearchResponse, JobListing}
import com.jobgun.config.WeaviateConfig

trait WeaviateSearchModel:
  def searchJobs(
      userEmbedding: Iterable[Double]
  ): IO[WeaviateSearchModel.WeaviateClientException, Chunk[JobListing]]
end WeaviateSearchModel

object WeaviateSearchModel:
  import scala.jdk.CollectionConverters.IterableHasAsScala
  import com.google.common.primitives.Floats

  class WeaviateClientException(message: String) extends Exception

  private val defaultClient = ZLayer {
    ZIO.succeed(WeaviateClient(WeaviateConfig.config))
  }

  val default = defaultClient >>> ZLayer {
    for client <- ZIO.service[WeaviateClient]
    yield new WeaviateSearchModel:
      def searchJobs(
          userEmbedding: Iterable[Double]
      ): IO[WeaviateClientException, Chunk[JobListing]] =
        val request =
          client.graphQL.get
            .withClassName("JobListing")
            .withFields(
              Field.builder.name("created").build,
              Field.builder.name("title").build,
              Field.builder.name("description").build,
              Field.builder.name("employment_type").build,
              Field.builder.name("location").build,
              Field.builder.name("url").build,
              Field.builder.name("company_name").build,
              Field.builder.name("company_url").build,
              Field.builder.name("country").build
            )
            .withNearVector(
              NearVectorArgument.builder
                .vector(
                  Floats
                    .asList(userEmbedding.map(_.toFloat).toSeq*)
                    .asScala
                    .toArray
                )
                .build
            )
            .withLimit(50)

        ZIO.logInfo(s"Running Weaviate search model") *> ZIO
          .succeed(request.run)
          .flatMap { (result: Result[GraphQLResponse]) =>
            com.google.gson
              .Gson()
              .toJson(result)
              .fromJson[WeaviateSearchResponse] match
              case Right(value) => ZIO.succeed(value)
              case Left(error) =>
                ZIO.fail(WeaviateClientException(error.toString))
          }
          .map(_.jobListings)
  }

  def searchJobs(userEmbedding: Iterable[Double]) =
    ZIO.serviceWithZIO[WeaviateSearchModel](
      _.searchJobs(userEmbedding)
    )

end WeaviateSearchModel
