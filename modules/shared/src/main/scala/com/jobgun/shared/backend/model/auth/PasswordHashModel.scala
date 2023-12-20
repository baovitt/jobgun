package com.jobgun.shared.backend
package model.auth

import zio.{ZIO, UIO}
import java.util.Base64
import java.security.{MessageDigest, SecureRandom}

object PasswordHashModel:
  def hashPassword(password: String): UIO[String] = ZIO.succeed {
    val hash = MessageDigest.getInstance("SHA-256").digest(password.getBytes)
    Base64.getEncoder().encodeToString(hash)
  }
end PasswordHashModel
