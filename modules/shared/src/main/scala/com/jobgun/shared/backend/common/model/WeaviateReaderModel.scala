package com.jobgun.shared.backend
package common.model

import io.weaviate.client as weaviate

// Weaviate Imports:
import weaviate.base.Result
import weaviate.WeaviateClient
import weaviate.v1.graphql.query.fields.Field
import weaviate.v1.graphql.model.GraphQLResponse
import weaviate.v1.filters.{Operator, WhereFilter}
import weaviate.v1.graphql.query.argument.{NearVectorArgument, WhereArgument}

// ZIO Imports:
import zio.{Chunk, IO, ZIO, ZLayer}
import zio.json.DecoderOps

// Jobgun Imports:
import search.domain.{WeaviateSearchResponse, ATSJobListing, SearchFilter}
import common.config.WeaviateConfig

trait WeaviateReaderModel:
  import WeaviateReaderModel.*

  def searchJobs(
      page: Int,
      pageSize: Int,
      userEmbedding: Iterable[Double],
      filters: Option[SearchFilter] = None
  ): IO[WeaviateClientException, Chunk[ATSJobListing]]
end WeaviateReaderModel

object WeaviateReaderModel:
  import scala.jdk.CollectionConverters.IterableHasAsScala
  import com.google.common.primitives.Floats

  class WeaviateClientException(message: String) extends Exception

  private val defaultClient = ZLayer {
    ZIO.succeed(WeaviateClient(WeaviateConfig.config))
  }

  val default = defaultClient >>> ZLayer {
    for client <- ZIO.service[WeaviateClient]
    yield new WeaviateReaderModel:
      def field(field: String, subFields: Field*) =
        Field.builder.name(field).fields(subFields*).build

      private def formSearchFilter(filter: SearchFilter) =
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
          filters: Option[SearchFilter] = None
      ): IO[WeaviateClientException, Chunk[ATSJobListing]] =
        val request =
          client.graphQL.get
            .withClassName("JobListing")
            .withFields(
              field("created"),
              field("title"),
              field("description"),
              field("employment_type"),
              field("location"),
              field("url"),
              field("company_name"),
              field("company_url"),
              field("country"),
              field("_additional", field("distance"))
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
      filters: Option[SearchFilter] = None
  ) =
    ZIO.serviceWithZIO[WeaviateReaderModel](
      _.searchJobs(page, pageSize, userEmbedding, filters)
    )

end WeaviateReaderModel
