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
import common.model.auth.PasswordHashModel
import recruit.domain.sql.{RecruiteeIntegrationTable, RecruiterAccountTable}

trait RecruiterPostgresModel[R, E]:
  // Create
  def create(
      firstName: String,
      lastName: String,
      email: String,
      password: String,
      company: String,
      companyUrl: String,
      resumeUrl: String
  ): ZIO[R, E, Int]

  // Delete
  def delete(id: Int): ZIO[R, E, Unit]

  // Update
  def update(
      id: Int,
      firstName: String,
      lastName: String,
      email: String,
      password: String,
      company: String,
      companyUrl: String,
      resumeUrl: String,
      isActive: Boolean
  ): ZIO[R, E, Unit]
  def verify(id: Int): ZIO[R, E, Unit]

  // Get
  def get(id: Int): ZIO[R, E, Option[RecruiterAccountTable]]
  def login(
      email: String,
      password: String
  ): ZIO[R, E, Option[RecruiterAccountTable]]
  def userExists(email: String): ZIO[R, E, Option[RecruiterAccountTable]]
end RecruiterPostgresModel

object RecruiterPostgresModel:
  val postgres: URLayer[PostgresConfig, RecruiterPostgresModel[
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
      yield new RecruiterPostgresModel[Any, SQLException]:
        import quill.*

        def create(
            firstName: String,
            lastName: String,
            email: String,
            password: String,
            company: String,
            companyUrl: String,
            resumeUrl: String
        ): ZIO[Any, SQLException, Int] =
          for
            hashedPwd <- PasswordHashModel.hashPassword(password)
            responseId <- run(
              query[RecruiterAccountTable]
                .insert(
                  _.firstName -> lift(firstName),
                  _.lastName -> lift(lastName),
                  _.email -> lift(email),
                  _.password -> lift(hashedPwd),
                  _.company -> lift(company),
                  _.companyUrl -> lift(companyUrl),
                  _.resumeUrl -> lift(resumeUrl),
                  _.isActive -> true,
                  _.isVerified -> false
                )
                .returningGenerated(_.id)
            )
          yield responseId

        def delete(id: Int): ZIO[Any, SQLException, Unit] =
          for _ <- run(
              query[RecruiterAccountTable]
                .filter(_.id == lift(id))
                .delete
            )
          yield ()

        def update(
            id: Int,
            firstName: String,
            lastName: String,
            email: String,
            password: String,
            company: String,
            companyUrl: String,
            resumeUrl: String,
            isActive: Boolean,
        ): ZIO[Any, SQLException, Unit] =
          for _ <- run(
              query[RecruiterAccountTable]
                .filter(_.id == lift(id))
                .update(
                  _.firstName -> lift(firstName),
                  _.lastName -> lift(lastName),
                  _.email -> lift(email),
                  _.password -> lift(password),
                  _.company -> lift(company),
                  _.companyUrl -> lift(companyUrl),
                  _.resumeUrl -> lift(resumeUrl),
                  _.isActive -> lift(isActive)
                )
            )
          yield ()

        def verify(id: Int): ZIO[Any, SQLException, Unit] =
          for _ <- run(
              query[RecruiterAccountTable]
                .filter(_.id == lift(id))
                .update(
                  _.isVerified -> true
                )
            )
          yield ()

        def get(
            id: Int
        ): ZIO[Any, SQLException, Option[RecruiterAccountTable]] =
          for user <- run(
              query[RecruiterAccountTable]
                .filter(_.id == lift(id))
            )
          yield user.headOption

        def login(
            email: String,
            password: String
        ): ZIO[Any, SQLException, Option[RecruiterAccountTable]] =
          for
            hashedPwd <- PasswordHashModel.hashPassword(password)
            userOption <- userExists(email).flatMap {
              case Some(user) if user.password == hashedPwd =>
                ZIO.succeed(Some(user))
              case _ => ZIO.succeed(None)
            }
          yield userOption

        def userExists(
            email: String
        ): ZIO[Any, SQLException, Option[RecruiterAccountTable]] =
          for user <- run(
              query[RecruiterAccountTable]
                .filter(_.email == lift(email))
            )
          yield user.headOption
    }
end RecruiterPostgresModel
