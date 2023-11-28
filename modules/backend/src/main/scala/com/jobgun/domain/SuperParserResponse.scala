package com.jobgun.domain

import zio.json.{JsonCodec, jsonField}
import zio.Chunk

final case class SuperParserResponse(
    status: String,
    data: SuperParserResponseData
) derives JsonCodec

final case class SuperParserResponseData(
    name: SuperParserResponseName,
    email: Chunk[SuperParserResponseEmail],
    phone: Chunk[SuperParserResponsePhone],
    employer: Chunk[Either[
      SuperParserResponseCurrentEmployer,
      SuperParserResponsePastEmployer
    ]],
    education: Chunk[Either[
      SuperParserResponseCurrentEducation,
      SuperParserResponsePastEducation
    ]],
    skills: SuperParserResponseSkills
) derives JsonCodec

final case class SuperParserResponseName(
    @jsonField("full_name") fullName: String,
    @jsonField("first_name") firstName: String,
    @jsonField("last_name") lastName: String
) derives JsonCodec

final case class SuperParserResponseEmail(email: String) derives JsonCodec
final case class SuperParserResponsePhone(phone: String) derives JsonCodec

final case class SuperParserResponseCurrentEmployer(
    @jsonField("company_name") companyName: Chunk[String],
    role: String,
    @jsonField("from_year") fromYear: Int,
    @jsonField("from_month") fromMonth: Int,
    @jsonField("is_current") isCurrent: Boolean,
    description: String,
    skills: Chunk[String]
) derives JsonCodec

final case class SuperParserResponsePastEmployer(
    @jsonField("company_name") companyName: Chunk[String],
    role: String,
    @jsonField("from_year") fromYear: Int,
    @jsonField("from_month") fromMonth: Int,
    @jsonField("to_year") toYear: Int,
    @jsonField("to_month") toMonth: Int,
    description: String,
    skills: Chunk[String]
) derives JsonCodec

final case class SuperParserResponsePastEducation(
    institute: String,
    degree: String,
    course: String,
    @jsonField("from_year") fromYear: Int,
    @jsonField("from_month") fromMonth: Int,
    @jsonField("to_year") toYear: Int,
    @jsonField("to_month") toMonth: Int
) derives JsonCodec

final case class SuperParserResponseCurrentEducation(
    institute: String,
    degree: String,
    course: String,
    @jsonField("from_year") fromYear: Int,
    @jsonField("from_month") fromMonth: Int,
    @jsonField("is_current") isCurrent: Boolean
) derives JsonCodec

final case class SuperParserResponseSkills(
    @jsonField("skills_meta") skills: Chunk[String],
    experience: Float,
    @jsonField("last_used_on") lastUsedOn: String,
    timeline: Chunk[SuperParserResponseSkillsMetaTimeline]
) derives JsonCodec

final case class SuperParserResponseSkillsMeta(
    @jsonField("display_name") name: String,
    experience: Float,
    @jsonField("last_used_on") lastUsedOn: String,
    timeline: Chunk[SuperParserResponseSkillsMetaTimeline]
) derives JsonCodec

final case class SuperParserResponseSkillsMetaTimeline(
    @jsonField("from_date") fromDate: String,
    @jsonField("to_date") toDate: String,
    source: Chunk[String]
) derives JsonCodec
