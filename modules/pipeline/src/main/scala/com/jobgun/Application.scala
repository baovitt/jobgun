package com.jobgun

import zio.*
import zio.json.*

import com.jobgun.shared.domain.{ATSJobListing, QueryableListing}

trait Application:
  val readListings = ZIO
    .readFile("samples.json")
    .map(_.fromJson[Chunk[ATSJobListing]])
    .absolve

  def writeListings(listings: Chunk[QueryableListing]) =
    ZIO.writeFile("output.json", listings.toJson)
end Application
