package com.jobgun.shared.domain

import zio.Chunk
import zio.json.JsonCodec

final case class JobDescription(
    educationRequirements: Chunk[JobDescription.EducationRequirement],
    skillRequirements: Chunk[JobDescription.SkillRequirement],
    credentialRequirements: Chunk[JobDescription.Credential],
    summary: String,
    highlights: Chunk[String]
) derives JsonCodec

object JobDescription:
  final case class EducationRequirement(
      requirementType: String,
      educationLevel: String,
      description: String
  ) derives JsonCodec

  final case class SkillRequirement(
      requirementType: String,
      skillType: String,
      minimumMonths: Int,
      title: String,
      description: String
  ) derives JsonCodec

  final case class Credential(
      requirementType: String,
      title: String
  ) derives JsonCodec
end JobDescription
