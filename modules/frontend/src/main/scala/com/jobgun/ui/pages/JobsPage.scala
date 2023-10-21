package com.jobgun.ui.pages

import com.raquo.laminar.api.L.*

import com.jobgun.ui.Event.{RoutingEvent, LandingPageEvent}
import com.raquo.laminar.modifiers.RenderableNode

import com.jobgun.ui.components.FrontNavbar
// import frontroute.*

import com.jobgun.ui.domain.GlobalState

import com.jobgun.ui.Event
import com.raquo.laminar.nodes.TextNode

import com.jobgun.ui.components.jobs.JobListing

object JobsPage:
  private def jobMatchColor(jobMatchPercent: Int) =
    val p: Double = jobMatchPercent / 100.0
    s"rgb(${((1.0 - p) * 178).toInt}, ${(p * 178).toInt}, 0)"

  def apply()(using
      eventBus: EventBus[Event],
      globalState: Signal[GlobalState]
  ) =
    val pageNum: Var[Int] = Var(1)
    val listingsSignal: Signal[Seq[Node]] = 
      globalState.map {
        case GlobalState(_, _, listings, _) if listings.nonEmpty =>
          listings.map { listing =>
            JobListing(
              listing.title,
              listing.companyName,
              listing.description,
              98,
              listing.location,
              listing.employmentType,
              listing.url
            )
          }
        case _ => List.empty
      }

    div(
      // FrontNavbar(),
      div(
        cls := "items-center w-full px-5 py-24 mx-auto md:px-12 lg:px-16 max-w-7xl",
        div(
          cls := "space-y-12 divide-y-2 divide-mercury-300",
          children <-- listingsSignal
        )
      ),
      div(
        cls := "items-center px-8 mx-auto max-w-7xl lg:px-16 md:px-12",
        div(
          cls := "justify-center w-full lg:p-10 max-auto",
          div(
            cls := "flex items-center justify-center px-4 py-3 bg-white sm:px-6",
            div(
              cls := "flex justify-center flex-1 gap-3",
              a(
                href := "#",
                cls := "inline-flex items-center justify-center p-3 text-sm text-gray-700 rounded-xl group ring-1 focus:outline-none ring-gray-200 hover:text-black hover:ring-blue-300 active:bg-gray-50 focus-visible:outline-gray-600 focus-visible:ring-gray-300",
                "First"
              ),
              a(
                href := "#",
                cls := "inline-flex items-center justify-center p-3 text-sm text-gray-700 rounded-xl group ring-1 focus:outline-none ring-gray-200 hover:text-black hover:ring-blue-300 active:bg-gray-50 focus-visible:outline-gray-600 focus-visible:ring-gray-300",
                "Previous"
              ),
              a(
                href := "#",
                cls := "inline-flex items-center justify-center p-3 text-sm text-gray-700 rounded-xl group ring-1 focus:outline-none ring-gray-200 hover:text-black hover:ring-blue-300 active:bg-gray-50 focus-visible:outline-gray-600 focus-visible:ring-gray-300",
                "Next"
              )
            )
          )
        )
      )
    )
end JobsPage
