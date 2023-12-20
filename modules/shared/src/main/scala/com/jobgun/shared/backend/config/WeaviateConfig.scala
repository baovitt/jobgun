package com.jobgun.shared.backend
package config

import io.weaviate.client.Config

object WeaviateConfig:
  final val config = Config("http", "jobgun-ryuzodtp.weaviate.network")
end WeaviateConfig
