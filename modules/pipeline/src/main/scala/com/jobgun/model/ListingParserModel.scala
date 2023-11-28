package com.jobgun.model

import zio.*
import zio.openai.*
import zio.openai.model.{OpenAIFailure, Temperature, CreateCompletionResponse}
import zio.openai.model.CreateCompletionRequest.{MaxTokens, Prompt}
import com.jobgun.shared.domain.{ATSJobListing, JobDescription}

trait ListingParserModel:
  def parseListing(
      listing: ATSJobListing
  ): ZIO[Completions, OpenAIFailure, (ATSJobListing, Option[JobDescription])]
end ListingParserModel

object ListingParserModel:
  import zio.json.*

  private def generateListingPrompt(listingDescription: String): String =
    s"""You're given the task of reading a job listing and extracting information to fill in the following fields:

        {
            "educationRequirements": [
                {
                    "requirementType": "string", // Options: "required", "preferred", "critical"
                    "educationLevel": "string", // Options: "high school", "associates", "bachelors", "masters", "doctorate"
                    "description": "string" // Describe the education requirement, e.g. "Bachelor's degree in Computer Science or related field"
                }
            ],
            "skillRequirements": [
                {
                    "requirementType": "string", // Options: "required", "preferred", "critical"
                    "skillType": "string", // Options: "operational", "soft"
                    "title": "string",
                    "minimumMonths": "integer", // Minimum months of experience required, 0 if N/A
                    "description": "string" // Describe the skill requirement, e.g. "Java programming" or "Communications with clients"
                }
            ],
            "credentialRequirements": [
                {
                    "requirementType": "string", // Options: "required", "preferred", "critical"
                    "title": "string"
                }
            ],
            "summary": "string", // A one-paragraph description of the job
            "highlights": [
                "string" // Important passages from the job summary; copy them directly
            ]
	    }
        Please extract the necessary information thoroughly from the job listing and fill in the JSON structure accordingly. Provide pure json, no other content. The job listing is:

        $listingDescription
        """.stripMargin

  private def runListingModelAndParse(
      listing: ATSJobListing,
      temperature: Temperature = Temperature(0.2),
      attempts: Int = 1
  ): ZIO[Completions, OpenAIFailure, Option[JobDescription]] =
    for
      result <- Completions.createCompletion(
        model = "text-davinci-003",
        prompt = Prompt.String(generateListingPrompt(listing.description)),
        temperature = temperature,
        maxTokens = MaxTokens(2500)
      )
      output = result.choices
        .map { completion =>
          println((completion.text getOrElse "").fromJson[JobDescription])
          (completion.text getOrElse "").fromJson[JobDescription]
        }
        .headOption
      parsedDescription <- output match
        case Some(Right(parsed)) => ZIO.succeed(Some(parsed))
        case _ if attempts < 3 =>
          runListingModelAndParse(
            listing,
            temperature =
              if (attempts == 1) Temperature(0.5) else Temperature(0.8),
            attempts + 1
          )
        case _ => ZIO.succeed(None)

    yield parsedDescription

  lazy val default = ZLayer {
    ZIO.succeed {
      new ListingParserModel:
        def parseListing(
            listing: ATSJobListing
        ): ZIO[
          Completions,
          OpenAIFailure,
          (ATSJobListing, Option[JobDescription])
        ] =
          runListingModelAndParse(listing, Temperature(0.2)).map(listing -> _)

    }
  }

  def parseListing(
      listing: ATSJobListing
  ): ZIO[
    ListingParserModel with Completions,
    OpenAIFailure,
    (ATSJobListing, Option[JobDescription])
  ] =
    ZIO.serviceWithZIO[ListingParserModel](_.parseListing(listing))
end ListingParserModel
