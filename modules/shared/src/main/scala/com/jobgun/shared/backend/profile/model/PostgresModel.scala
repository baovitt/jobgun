package com.jobgun.shared.backend
package profile.model

// Quill Imports:
import io.getquill.*
import io.getquill.jdbczio.*
import io.getquill.context.ZioJdbc.*

// ZIO Imports:
import zio.*

// Java Imports:
import java.sql.SQLException
import javax.sql.DataSource
import javax.naming.InitialContext

// PostgreSQL Imports:
import org.postgresql.jdbc3.Jdbc3PoolingDataSource

// Jobgun Imports:
import profile.domain.sql.UserAccountTable
import common.config.PostgresConfig
import common.model.auth.PasswordHashModel

trait PostgresModel[R, E]:
  // Create
  def create(user: UserAccountTable): ZIO[R, E, Int]

  // Delete
  def delete(id: Int): ZIO[R, E, Unit]

  // Update
  def update(id: Int, user: UserAccountTable): ZIO[R, E, Unit]
  def verify(id: Int): ZIO[R, E, Unit]

  // Get
  def get(id: Int): ZIO[R, E, Option[UserAccountTable]]
  def login(
      email: String,
      password: String
  ): ZIO[R, E, Option[UserAccountTable]]
  def userExists(email: String): ZIO[R, E, Option[UserAccountTable]]
end PostgresModel

object PostgresModel:
  val postgres: URLayer[PostgresConfig, PostgresModel[
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
      yield new PostgresModel[Any, SQLException]:
        import quill.*

        // Create
        def create(user: UserAccountTable): ZIO[Any, SQLException, Int] =
          for
            hashedPwd <- PasswordHashModel.hashPassword(user.password)
            responseId <- run(
              query[UserAccountTable]
                .insert(
                  _.firstName -> lift(user.firstName),
                  _.lastName -> lift(user.lastName),
                  _.email -> lift(user.email),
                  _.password -> lift(hashedPwd),
                  _.resumeUrl -> lift(user.resumeUrl),
                  _.isVerified -> lift(false)
                )
                .returningGenerated(_.id)
            )
          yield responseId

        // Delete
        def delete(id: Int): ZIO[Any, SQLException, Unit] =
          for _ <- run(
              query[UserAccountTable]
                .filter(_.id == lift(id))
                .delete
            )
          yield ()

        // Update
        def update(
            id: Int,
            user: UserAccountTable
        ): ZIO[Any, SQLException, Unit] =
          for _ <- run(
              query[UserAccountTable]
                .filter(_.id == lift(id))
                .update(
                  _.firstName -> lift(user.firstName),
                  _.lastName -> lift(user.lastName),
                  _.email -> lift(user.email),
                  _.password -> lift(user.password),
                  _.resumeUrl -> lift(user.resumeUrl),
                  _.isVerified -> lift(user.isVerified)
                )
            )
          yield ()

        def verify(id: Int): ZIO[Any, SQLException, Unit] =
          for _ <- run(
              query[UserAccountTable]
                .filter(_.id == lift(id))
                .update(
                  _.isVerified -> lift(true)
                )
            )
          yield ()

        // Get
        def get(id: Int): ZIO[Any, SQLException, Option[UserAccountTable]] =
          for user <- run(
              query[UserAccountTable]
                .filter(_.id == lift(id))
            )
          yield user.headOption

        def login(
            email: String,
            password: String
        ): ZIO[Any, SQLException, Option[UserAccountTable]] =
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
        ): ZIO[Any, SQLException, Option[UserAccountTable]] =
          for user <- run(
              query[UserAccountTable]
                .filter(_.email == lift(email))
            )
          yield user.headOption
    }
end PostgresModel
