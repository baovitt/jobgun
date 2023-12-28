package com.jobgun.shared.backend
package common.config

// ZIO Imports:
import zio.*

// Config Imports:
import zio.config.*
import zio.config.syntax.*
import zio.config.magnolia.*
import com.typesafe.config.ConfigFactory
import zio.config.typesafe.TypesafeConfigSource
import zio.config.typesafe.TypesafeConfigSource.fromTypesafeConfig

// PostgreSQL Imports:
import org.postgresql.jdbc3.Jdbc3PoolingDataSource

final case class PostgresConfig(
    server_name: String,
    database_name: String,
    user: String,
    password: String,
    max_connections: Int
):
  self =>

  val source: UIO[Jdbc3PoolingDataSource] = ZIO.succeed {
    val ds = new Jdbc3PoolingDataSource()
    ds.setDataSourceName("PostgreSQL Data Source")
    ds.setServerName(self.server_name)
    ds.setDatabaseName(self.database_name)
    ds.setUser(self.user)
    ds.setPassword(self.password)
    ds.setMaxConnections(self.max_connections)
    ds
  }
end PostgresConfig

object PostgresConfig:
  final val live: ZLayer[Any, ReadError[String], PostgresConfig] =
    ZLayer {
      read {
        descriptor[PostgresConfig].from(
          TypesafeConfigSource.fromResourcePath
            .at(PropertyTreePath.$("postgres"))
        )
      }
    }
end PostgresConfig
