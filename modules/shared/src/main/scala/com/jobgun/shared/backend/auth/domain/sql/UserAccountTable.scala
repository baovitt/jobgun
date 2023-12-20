package com.jobgun.shared.backend.auth
package domain.sql

final case class UserAccountTable(
    id: Int = 0,
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    isVerified: Boolean
)
