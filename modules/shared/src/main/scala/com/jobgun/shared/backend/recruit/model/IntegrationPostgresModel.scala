package com.jobgun.shared.backend
package recruit.model

// Quill Imports:
import io.getquill.*
import io.getquill.jdbczio.*
import io.getquill.context.ZioJdbc.*

// ZIO Imports:
import zio.*

// Java Imports:
import java.util.Date
import java.sql.SQLException
import javax.sql.DataSource
import javax.naming.InitialContext

// PostgreSQL Imports:
import org.postgresql.jdbc3.Jdbc3PoolingDataSource

// Jobgun Imports:
import common.config.PostgresConfig
import recruit.domain.sql.{RecruiteeIntegrationTable, RecruiterAccountTable}

trait IntegrationPostgresModel[R, E]:
  // Create
  def createRecruiteeIntegration(
      recruiterId: Int,
      companyId: String,
      recruiteeToken: String,
      lastVerified: Date
  ): ZIO[R, E, Int]

  // Read
  def readRecruiteeIntegration(
      recruiterId: Int
  ): ZIO[R, E, Option[RecruiteeIntegrationTable]]

  // Update
  def updateRecruiteeIntegration(
      recruiterId: Int,
      companyId: String,
      recruiteeToken: String,
      isActive: Boolean,
      lastVerified: Date
  ): ZIO[R, E, Unit]

  // Delete
  def deleteRecruiteeIntegration(recruiterId: Int): ZIO[R, E, Long]
end IntegrationPostgresModel

object IntegrationPostgresModel:
  val live: URLayer[PostgresConfig, IntegrationPostgresModel[
    Any,
    SQLException
  ]] =
    ZLayer {
      for
        conf <- ZIO.service[PostgresConfig]
        source <- conf.source
        quill = Quill.Postgres[SnakeCase](
          SnakeCase,
          source
        )
      yield new IntegrationPostgresModel[Any, SQLException]:
        import quill.*

        def createRecruiteeIntegration(
            recruiterId: Int,
            companyId: String,
            recruiteeToken: String,
            lastVerified: Date
        ): ZIO[Any, SQLException, Int] =
          run(
            query[RecruiteeIntegrationTable]
              .insert(
                _.recruiterId -> lift(recruiterId),
                _.companyId -> lift(companyId),
                _.recruiteeToken -> lift(recruiteeToken),
                _.isActive -> true,
                _.lastVerified -> lift(lastVerified)
              )
              .returningGenerated(_.id)
          )

        def readRecruiteeIntegration(
            recruiterId: Int
        ): ZIO[Any, SQLException, Option[RecruiteeIntegrationTable]] =
          run(
            query[RecruiteeIntegrationTable].filter(
              _.recruiterId == lift(recruiterId)
            )
          ).map(_.headOption)

        def updateRecruiteeIntegration(
            recruiterId: Int,
            companyId: String,
            recruiteeToken: String,
            isActive: Boolean,
            lastVerified: Date
        ): ZIO[Any, SQLException, Unit] =
          run(
            query[RecruiteeIntegrationTable]
              .filter(_.recruiterId == lift(recruiterId))
              .update(
                _.companyId -> lift(companyId),
                _.recruiteeToken -> lift(recruiteeToken),
                _.isActive -> lift(isActive),
                _.lastVerified -> lift(lastVerified)
              )
          ).map(_ => ())

        def deleteRecruiteeIntegration(
            recruiterId: Int
        ): ZIO[Any, SQLException, Long] =
          run(
            query[RecruiteeIntegrationTable]
              .filter(_.recruiterId == lift(recruiterId))
              .delete
          )
    }
end IntegrationPostgresModel
