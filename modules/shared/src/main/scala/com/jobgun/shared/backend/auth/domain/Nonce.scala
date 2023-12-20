package com.jobgun.shared.backend.auth
package domain

sealed trait Nonce

object Nonce:
  final case class SignUpNonce(token: String) extends Nonce
  final case class ResetPasswordNonce(token: String) extends Nonce

  type UserId = Int
end Nonce
