package com.jobgun.shared.backend
package common.config

// ZIO Imports:
import zio.*
import zio.config.*
import zio.config.magnolia.*
import zio.config.syntax.*
import zio.config.typesafe.TypesafeConfigSource
import zio.config.typesafe.TypesafeConfigSource.fromTypesafeConfig
import zio.aws.core.config.{AwsConfig, CommonAwsConfig}
import zio.aws.netty.NettyHttpClient

// AWS Imports:
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.auth.credentials.{
  AwsBasicCredentials,
  StaticCredentialsProvider
}

// Typesafe Imports:
import com.typesafe.config.ConfigFactory

// Java Imports:
import java.net.URI

final case class ObjectStorageConfig(
    region: String,
    endpoint: String,
    accessKey: String,
    secretKey: String
)

object ObjectStorageConfig:
  final val live: ZLayer[Any, ReadError[String], ObjectStorageConfig] =
    ZLayer {
      read {
        descriptor[ObjectStorageConfig].from(
          TypesafeConfigSource.fromResourcePath
            .at(PropertyTreePath.$("bucket"))
        )
      }
    }

  final val zioAwsConfig: ZLayer[ObjectStorageConfig, Throwable, AwsConfig] = {
    val configLayer: ZLayer[ObjectStorageConfig, Nothing, CommonAwsConfig] =
      ZLayer.fromZIO {
        for config <- ZIO.service[ObjectStorageConfig]
        yield CommonAwsConfig(
          Some(Region.of(config.region)),
          StaticCredentialsProvider.create(
            AwsBasicCredentials.create(
              config.accessKey,
              config.secretKey
            )
          ),
          Some(URI(config.endpoint)),
          None
        )
      }

    (configLayer ++ NettyHttpClient.default) >>> AwsConfig
      .configured()
      .passthrough
  }
end ObjectStorageConfig
