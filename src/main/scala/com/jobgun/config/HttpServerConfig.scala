package com.jobgun.config

import io.netty.channel.{ChannelFactory, ServerChannel}
import zhttp.service.EventLoopGroup
import zhttp.service.server.ServerChannelFactory
import zio.ULayer

type HttpServerSettings = ChannelFactory[ServerChannel] & EventLoopGroup

object HttpServerSettings:
  val default: ULayer[HttpServerSettings] =
    EventLoopGroup.auto(0) ++ ServerChannelFactory.auto
end HttpServerSettings
