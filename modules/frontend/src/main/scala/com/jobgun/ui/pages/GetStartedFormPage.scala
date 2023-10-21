package com.jobgun.ui.pages

import com.raquo.laminar.api.L.*
import com.jobgun.ui.components.{FileUploader, FrontNavbar}
import com.jobgun.ui.components.tryfree.ResumeForm
import com.jobgun.shared.domain.JobListing
import sttp.tapir.TapirFile
// import frontroute.*

import com.jobgun.ui.Event
import com.jobgun.ui.domain.GlobalState

object GetStartedFormPage:
  def apply()(using
      eventBus: EventBus[Event],
      globalState: Signal[GlobalState]
  ) =
    val hitNext: Var[Boolean] = Var(false)
    div(
      FrontNavbar(),
      child <-- globalState.combineWith(hitNext.signal).map {
        case (GlobalState(_, _, jobListings, _), true) if jobListings.nonEmpty =>
          JobsPage()
        case _ => emptyNode
      },
      div(
        cls <-- globalState.combineWith(hitNext.signal).map {
          case (GlobalState(_, _, jobListings, _), true) if jobListings.nonEmpty => "hidden"
          case _ =>
            "items-center w-full px-5 py-24 mx-auto md:px-12 lg:px-16 max-w-7xl"
        },
        div(
          cls := "grid items-start grid-cols-1 md:grid-cols-2",
          div(
            h2(
              cls := "mt-6 text-3xl font-medium text-black select-none",
              "Let's Find Your Next Opportunity"
            ),
            ResumeForm(hitNext)
            
          ),
          div(
            cls := "h-full mt-12 lg:mt-0 border-mercury-400 lg:pl-24 md:border-l md:pl-12",
            div(
              cls := "flex flex-col justify-center m-auto",
              div(
                cls := "flex flex-col justify-center text-center md:flex-row md:text-left",
                div(
                  cls := "flex flex-col justify-center max-w-2xl p-10 space-y-12",
                  articleTag(
                    span(
                      cls := "inline-flex items-center text-black rounded-xl",
                      span(
                        cls := "font-mono text-sm",
                        "01"
                      )
                    ),
                    div(
                      cls := "mt-3 text-3xl tracking-tighter text-black",
                      "Basic Information"
                    ),
                    div(
                      cls := "mt-4 text-gray-500",
                      "Name, biography, etc."
                    )
                  ),
                  articleTag(
                    span(
                      cls := "inline-flex items-center text-black rounded-xl",
                      span(
                        cls := "font-mono text-sm",
                        "02"
                      )
                    ),
                    div(
                      cls := "mt-3 text-3xl tracking-tighter text-black",
                      "Experience & Education"
                    ),
                    div(
                      cls := "mt-4 text-gray-500",
                      "Work history, education, and certifications."
                    )
                  ),
                  articleTag(
                    span(
                      cls := "inline-flex items-center text-black rounded-xl",
                      span(
                        cls := "font-mono text-sm",
                        "03"
                      )
                    ),
                    div(
                      cls := "mt-3 text-3xl tracking-tighter text-black",
                      "Other Information"
                    ),
                    div(
                      cls := "mt-4 text-gray-500",
                      "Skills, interests, and other information."
                    )
                  )
                )
              )
            )
          )
        )
      )
    )
end GetStartedFormPage
