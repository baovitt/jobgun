package com.jobgun.shared.backend
package common.config

// ZIO Imports:
import zio.*

// Config Imports:
import zio.config.*
import zio.config.magnolia.*
import zio.config.syntax.*
import zio.config.typesafe.TypesafeConfigSource
import zio.config.typesafe.TypesafeConfigSource.fromTypesafeConfig
import com.typesafe.config.ConfigFactory

final case class ResendConfig(bearer: String)

object ResendConfig:
  final val live: ZLayer[Any, ReadError[String], ResendConfig] =
    ZLayer {
      read {
        descriptor[ResendConfig].from(
          TypesafeConfigSource.fromResourcePath
            .at(PropertyTreePath.$("resend"))
        )
      }
    }
end ResendConfig
