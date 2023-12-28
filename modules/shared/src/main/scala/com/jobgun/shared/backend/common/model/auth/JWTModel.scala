package com.jobgun.shared.backend
package common.model.auth

// JWT Imports:
import pdi.jwt.JwtAlgorithm
import pdi.jwt.JwtClaim
import pdi.jwt.JwtZIOJson

// ZIO Imports:
import zio.{ZIO, Task, UIO}
import zio.json.{JsonCodec, EncoderOps, DecoderOps}

// Java Imports:
import java.time.{Instant, Clock}

object JWTModel:
  given clock: Clock = Clock.systemUTC

  final case class Jwt(id: Int) derives JsonCodec

  private val key = "The government can suck my dick"
  private val algo = JwtAlgorithm.HS512

  def decode(jwt: String): Task[Jwt] =
    ZIO
      .fromTry(JwtZIOJson.decodeAll(jwt, key, Seq(algo)))
      .map(_._3.fromJson[Jwt])
      .absolve
      .mapError(_ => new Exception("invalid jwt"))

  def encode(id: Int): UIO[String] = ZIO.succeed {
    JwtZIOJson.encode(
      JwtClaim(Jwt(id).toJson).issuedNow.expiresIn(60 * 60 * 24),
      key,
      algo
    )
  }
end JWTModel
