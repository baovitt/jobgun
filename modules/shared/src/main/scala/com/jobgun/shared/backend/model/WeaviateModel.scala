package com.jobgun.shared.backend
package model

// ZIO Imports:
import zio.{Chunk, IO, ZIO, ZLayer}
import zio.json.DecoderOps

// Weaviate Imports:
import io.weaviate.client.base.Result
import io.weaviate.client.WeaviateClient
import io.weaviate.client.v1.graphql.query.argument.{
  NearVectorArgument,
  WhereArgument
}
import io.weaviate.client.v1.graphql.query.fields.Field
import io.weaviate.client.v1.graphql.model.GraphQLResponse
import io.weaviate.client.v1.filters.{Operator, WhereFilter}

// Jobgun Imports:
import com.jobgun.shared.backend.domain.WeaviateSearchResponse
import com.jobgun.shared.backend.domain.ATSJobListing
import com.jobgun.shared.backend.domain.requests.JobSearchWithEmbeddingRequest.JobSearchFilters
import com.jobgun.shared.backend.config.WeaviateConfig

trait WeaviateModel:
  def searchJobs(
      page: Int,
      pageSize: Int,
      userEmbedding: Iterable[Double],
      filters: Option[JobSearchFilters] = None
  ): IO[WeaviateModel.WeaviateClientException, Chunk[ATSJobListing]]
end WeaviateModel

object WeaviateModel:
  import scala.jdk.CollectionConverters.IterableHasAsScala
  import com.google.common.primitives.Floats

  class WeaviateClientException(message: String) extends Exception

  private val defaultClient = ZLayer {
    ZIO.succeed(WeaviateClient(WeaviateConfig.config))
  }

  val default = defaultClient >>> ZLayer {
    for client <- ZIO.service[WeaviateClient]
    yield new WeaviateModel:
      private def formSearchFilter(filter: JobSearchFilters) =
        def filterEquals(path: String, value: String) =
          WhereFilter.builder
            .path(path)
            .operator(Operator.Equal)
            .valueText(value)
            .build()

        val jobTypeFilters =
          List(
            filter.jobType.fullTime -> "Full-time",
            filter.jobType.partTime -> "Part-time",
            filter.jobType.contract -> "Contract",
            filter.jobType.internship -> "Internship",
            filter.jobType.other -> "Other"
          ).collect { case (true, value) =>
            filterEquals("employment_type", value)
          }

        WhereFilter
          .builder()
          .operands(jobTypeFilters*)
          .operator(Operator.Or)
          .build();

      def searchJobs(
          page: Int,
          pageSize: Int,
          userEmbedding: Iterable[Double],
          filters: Option[JobSearchFilters] = None
      ): IO[WeaviateClientException, Chunk[ATSJobListing]] =
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
              Field.builder.name("country").build,
              Field
                .builder()
                .name("_additional")
                .fields(Field.builder().name("distance").build())
                .build()
            )
            .withOffset(page * pageSize)
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
            .withLimit(pageSize)

        val requestWithFilters = filters match
          case Some(filter) => request.withWhere(formSearchFilter(filter))
          case None         => request

        ZIO
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

  def searchJobs(
      page: Int,
      pageSize: Int,
      userEmbedding: Iterable[Double],
      filters: Option[JobSearchFilters] = None
  ) =
    ZIO.serviceWithZIO[WeaviateModel](
      _.searchJobs(page, pageSize, userEmbedding, filters)
    )

end WeaviateModel
