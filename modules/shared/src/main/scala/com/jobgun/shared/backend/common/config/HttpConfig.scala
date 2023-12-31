package com.jobgun.shared.backend
package common.config

// ZIO Imports:
import zio.*
import zio.config.*
import zio.config.magnolia.*
import zio.config.syntax.*
import zio.config.typesafe.TypesafeConfigSource
import zio.config.typesafe.TypesafeConfigSource.fromTypesafeConfig

// Typesafe Imports:
import com.typesafe.config.ConfigFactory

final case class HttpConfig(port: Int, host: String)

object HttpConfig:
  final val live: ZLayer[Any, ReadError[String], HttpConfig] =
    ZLayer {
      read {
        descriptor[HttpConfig].from(
          TypesafeConfigSource.fromResourcePath
            .at(PropertyTreePath.$("http"))
        )
      }
    }
end HttpConfig
