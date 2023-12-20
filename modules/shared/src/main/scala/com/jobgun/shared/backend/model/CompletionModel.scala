package com.jobgun.shared.backend
package model

// ZIO Imports:
import zio.{ZIO, ZLayer}
import zio.json.DecoderOps
import zio.openai.Completions
import zio.openai.model.{OpenAIFailure, Temperature, CreateCompletionResponse}
import zio.openai.model.CreateCompletionRequest.{MaxTokens, Prompt}

// Jobgun Imports:
import com.jobgun.shared.backend.domain.ParsedJobDescription

trait CompletionModel:
  def parseUser(
      user: String
  ): ZIO[Any, OpenAIFailure, Option[ParsedJobDescription]]
end CompletionModel

object CompletionModel:

  private def generateUserPrompt(user: String): String =
    s"""
You're given the task of parsing a users resume into an ideal job listing to fill in the following fields:

{
    "required/preferred certifications": [], // What specific certifications does the user have?
    "required/preferred education": [], // What specific education does the user have?
    "required/preferred experience": [], // What specific work or military experience does the user have? Include years of experience for each item if mentioned.
    "required/preferred licenses": [], // What specific licenses does the user have
    "required/preferred security credentials": [], // What specific security credentials does this user have?
    "required/preferred misc requirements": [] // What misc qualifications does this user have? For example, patents, publications, achievements, skills, or training.
}

In any mention of the word "present" as a indication of time, the present date is ${java.time.LocalDate
        .now()
        .format(
          java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )} and the word "present" should be replaced with the current date. For example, "2015 – Present" should become "2015 - ${java.time.LocalDate
        .now()
        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))}"

Additionally, replace any of known company names with the role they worked in there, for example, spotify might become -> "music streaming" if their role was related to streaming technology. Finally, replace any mention of location with "Location".

Please extract the necessary information thoroughly from the users resume and fill in the JSON structure accordingly. Provide pure json, no other content. You're doing a great job! The users resume is:

$user
        """.stripMargin

  private def runUserModelAndParse(
      user: String,
      temperature: Temperature = Temperature(0.2),
      attempts: Int = 1
  )(
      completions: Completions
  ): ZIO[Any, OpenAIFailure, Option[ParsedJobDescription]] =
    for
      result <-
        completions.createCompletion(
          model = "text-davinci-003",
          prompt = Prompt.String(generateUserPrompt(user)),
          temperature = temperature,
          maxTokens = MaxTokens(2500)
        )
      output =
        result.choices.headOption
          .flatMap(completion =>
            (completion.text getOrElse "")
              .fromJson[ParsedJobDescription]
              .toOption
          )
      next <- output match
        case Some(_) => ZIO.succeed(output)
        case None if attempts == 1 =>
          runUserModelAndParse(user, Temperature(0.5), attempts + 1)(
            completions
          )
        case None if attempts == 2 =>
          runUserModelAndParse(user, Temperature(0.8), attempts + 1)(
            completions
          )
        case None => ZIO.succeed(None)
    yield next

  val default = ZLayer {
    for completions <- ZIO.service[Completions]
    yield new CompletionModel:
      def parseUser(
          user: String
      ): ZIO[Any, OpenAIFailure, Option[ParsedJobDescription]] =
        runUserModelAndParse(user)(completions)

  }

  def parseUser(
      user: String
  ): ZIO[CompletionModel with Completions, OpenAIFailure, Option[
    ParsedJobDescription
  ]] =
    ZIO.serviceWithZIO[CompletionModel](_.parseUser(user))
end CompletionModel
