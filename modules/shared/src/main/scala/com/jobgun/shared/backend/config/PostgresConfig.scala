package com.jobgun.shared.backend
package config

import zio.*
import zio.config.*
import zio.config.magnolia.*
import zio.config.syntax.*
import zio.config.typesafe.TypesafeConfigSource
import zio.config.typesafe.TypesafeConfigSource.fromTypesafeConfig
import com.typesafe.config.ConfigFactory

final case class PostgresConfig(
    server_name: String,
    database_name: String,
    user: String,
    password: String,
    max_connections: Int
)

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
