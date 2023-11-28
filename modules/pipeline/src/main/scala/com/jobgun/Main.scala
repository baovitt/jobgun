package com.jobgun

// ZIO Imports:
import zio.*
import zio.openai.{Embeddings, Completions}

import com.jobgun.model.ListingParserModel
import com.jobgun.shared.model.*
import com.jobgun.shared.domain.*

object Main extends ZIOAppDefault with Application:
  self =>

  def run = logic.provide(
    ListingParserModel.default,
    Embeddings.default,
    Completions.default,
    EmbeddingModel.default,
    ZLayer.Debug.tree
  )

  val logic = ZIO.scoped {
    for
      listings <- self.readListings
      _ <- Console.printLine(s"Read listings")
      parsedListings <- ZIO.foreachPar(listings) { listing =>
        ListingParserModel.parseListing(listing)
      }
      _ <- Console.printLine(s"parsed listings")

      // do something with these
      failed = parsedListings.collect { case (atsListing, None) =>
        atsListing
      }
      _ <- Console.printLine(s"Failed to parse ${failed.length} listings")

      succeeded = parsedListings.collect {
        case (atsListing, Some(description)) => (atsListing, description)
      }
      _ <- Console.printLine(s"Succeeded to parse ${succeeded.length} listings")

      queryableListings <- ZIO.foreachPar(succeeded) {
        case (atsListing, description) =>
          JobDescriptionWithEmbeddings.fromDescription(description).map {
            embeddedDescription =>
              QueryableListing(atsListing, embeddedDescription)
          }
      }
      _ <- Console.printLine(s"Generated Queryable listings")

      _ <- self.writeListings(queryableListings)
    yield ()
  }

end Main
