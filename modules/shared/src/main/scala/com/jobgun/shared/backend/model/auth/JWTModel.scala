package com.jobgun.shared.backend
package model.auth

import pdi.jwt.JwtAlgorithm
import pdi.jwt.JwtClaim
import pdi.jwt.JwtZIOJson
import zio.*
import zio.json.*

import java.time.{Instant, Clock}

object JWTModel:
  given clock: Clock = Clock.systemUTC

  final case class Jwt(id: Int) derives JsonCodec

  private val key = "The government can suck my dick"
  private val algo = JwtAlgorithm.HS512

  def decode(jwt: String) =
    ZIO
      .fromTry(JwtZIOJson.decodeAll(jwt, key, Seq(algo)))
      .map(_._3.fromJson[Jwt])
      .absolve

  def encode(id: Int) = ZIO.succeed {
    JwtZIOJson.encode(
      JwtClaim(Jwt(id).toJson).issuedNow.expiresIn(60 * 60 * 24),
      key,
      algo
    )
  }
end JWTModel
