package com.jobgun.shared.domain

import zio.Chunk
import zio.json.JsonCodec

final case class JobDescriptionWithEmbeddings(
    educationRequirements: Chunk[
      JobDescriptionWithEmbeddings.EducationRequirement
    ],
    skillRequirements: Chunk[JobDescriptionWithEmbeddings.SkillRequirement],
    credentialRequirements: Chunk[JobDescriptionWithEmbeddings.Credential],
    summary: String,
    highlights: Chunk[String]
) derives JsonCodec

object JobDescriptionWithEmbeddings:
  import zio.*
  import zio.openai.model.OpenAIFailure
  import com.jobgun.shared.model.EmbeddingModel

  def fromDescription(
      jobDescription: JobDescription
  ): ZIO[EmbeddingModel, OpenAIFailure, JobDescriptionWithEmbeddings] =
    val educationRequirements = jobDescription.educationRequirements.map {
      requirement =>
        EmbeddingModel.embedText(requirement.description).map { embeddings =>
          EducationRequirement(
            requirement.requirementType,
            requirement.educationLevel,
            requirement.description,
            embeddings
          )
        }
    }

    val skillRequirements = jobDescription.skillRequirements.map {
      requirement =>
        EmbeddingModel.embedText(requirement.description).map { embeddings =>
          SkillRequirement(
            requirement.requirementType,
            requirement.skillType,
            requirement.minimumMonths,
            requirement.title,
            requirement.description,
            embeddings
          )
        }
    }

    val credentialRequirements = jobDescription.credentialRequirements.map {
      requirement =>
        EmbeddingModel.embedText(requirement.title).map { embeddings =>
          Credential(
            requirement.requirementType,
            requirement.title,
            embeddings
          )
        }
    }

    for
      educationRequirements <- ZIO.collectAllPar(educationRequirements)
      skillRequirements <- ZIO.collectAllPar(skillRequirements)
      credentialRequirements <- ZIO.collectAllPar(credentialRequirements)
    yield JobDescriptionWithEmbeddings(
      educationRequirements,
      skillRequirements,
      credentialRequirements,
      jobDescription.summary,
      jobDescription.highlights
    )

  final case class EducationRequirement(
      requirementType: String,
      educationLevel: String,
      description: String,
      embeddings: Chunk[Double]
  ) derives JsonCodec

  final case class SkillRequirement(
      requirementType: String,
      skillType: String,
      minimumMonths: Int,
      title: String,
      description: String,
      embeddings: Chunk[Double]
  ) derives JsonCodec

  final case class Credential(
      requirementType: String,
      title: String,
      embeddings: Chunk[Double]
  ) derives JsonCodec
end JobDescriptionWithEmbeddings
