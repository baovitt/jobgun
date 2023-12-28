package com.jobgun.shared.backend
package common.utils

// ZIO Imports:
import zio.{ZIO, UIO, ZLayer, Duration, URIO, Clock}
import zio.stm.TMap

// Java Imports:
import java.time.Instant

class ExpiringCache[K, V](tmap: TMap[K, (V, Instant)], duration: Duration):
  def put(key: K, value: V): UIO[Unit] =
    ZIO.succeed(Instant.now()).flatMap { now =>
      tmap.put(key, (value, now.plusMillis(duration.toMillis))).commit
    }

  def get(key: K): UIO[Option[V]] =
    tmap.get(key).commit.map {
      case Some((value, expiration)) if Instant.now.isBefore(expiration) =>
        Some(value)
      case _ => None
    }

  private def cleanup: UIO[Unit] =
    tmap
      .retainIf { case (_, (_, expiration)) =>
        Instant.now.isBefore(expiration)
      }
      .commit
      .map(_ => ())

  def startCleanup(interval: Duration): URIO[Clock, Unit] =
    (cleanup.delay(interval).forever).fork.unit
end ExpiringCache

object ExpiringCache:
  def make[K, V](duration: Duration): UIO[ExpiringCache[K, V]] =
    TMap.empty[K, (V, Instant)].commit.map(new ExpiringCache(_, duration))
end ExpiringCache
