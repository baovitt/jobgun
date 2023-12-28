package com.jobgun.shared.backend
package recruit.domain

sealed trait Nonce

object Nonce:
  final case class SignUpNonce(token: String) extends Nonce
  final case class ResetPasswordNonce(token: String) extends Nonce

  type RecruiterId = Int
end Nonce
