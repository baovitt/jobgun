package com.jobgun.utils.extensions

// Armeria Imports:
import com.linecorp.armeria.server.ServerBuilder

// STTP Imports:
import sttp.tapir.server.armeria.TapirService
import sttp.capabilities.zio.ZioStreams

object ArmeriaServerExtensions:
  type ServiceType = [A] =>> zio.Task[A]

  extension (server: ServerBuilder)
    def services(
        serviceList: TapirService[ZioStreams, ServiceType]*
    ): ServerBuilder =
      serviceList.foldLeft(server)(_ service _)
end ArmeriaServerExtensions
