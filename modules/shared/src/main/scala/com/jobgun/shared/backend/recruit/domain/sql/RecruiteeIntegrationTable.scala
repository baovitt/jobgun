package com.jobgun.shared.backend
package recruit.domain.sql

final case class RecruiteeIntegrationTable(
    id: Int = 0,
    recruiterId: Int,
    companyId: String,
    recruiteeToken: String,
    isActive: Boolean,
    lastVerified: java.util.Date
)
