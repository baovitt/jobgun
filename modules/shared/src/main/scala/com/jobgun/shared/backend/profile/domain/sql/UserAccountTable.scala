package com.jobgun.shared.backend
package profile.domain.sql

final case class UserAccountTable(
    id: Int = 0,
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    resumeUrl: String,
    isVerified: Boolean
)
