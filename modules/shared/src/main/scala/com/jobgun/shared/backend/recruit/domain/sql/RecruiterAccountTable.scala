package com.jobgun.shared.backend
package recruit.domain.sql

final case class RecruiterAccountTable(
    id: Int = 0,
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    company: String,
    companyUrl: String,
    resumeUrl: String,
    recruiteeIntegrated: Boolean,
    isActive: Boolean,
    isVerified: Boolean
)
