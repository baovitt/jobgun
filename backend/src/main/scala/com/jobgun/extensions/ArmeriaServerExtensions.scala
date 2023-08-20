package com.jobgun.extensions

// Armeria Imports:
import com.linecorp.armeria.server.ServerBuilder

// STTP Imports:
import sttp.tapir.server.armeria.TapirService
import sttp.capabilities.zio.ZioStreams

object ArmeriaServerExtensions:
  type ServiceType = [A] =>> zio.RIO[Any, A]

  extension (server: ServerBuilder)
    def services(
        serviceList: List[TapirService[ZioStreams, ServiceType]]
    ): ServerBuilder =
      serviceList.foldLeft(server)(_ service _)
end ArmeriaServerExtensions
