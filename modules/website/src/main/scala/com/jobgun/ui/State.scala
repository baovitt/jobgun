package com.jobgun.ui

import com.raquo.laminar.api.L.*

import com.jobgun.ui.components.FrontNavbar
import com.jobgun.ui.pages.*

import org.scalajs.dom.File

import frontroute.*

object State:
  val root =
    div(
      initRouting,
      pathEnd {
        LandingPage()
      },
      path("about") {
        AboutPage()
      },
      path("get-started") {
        GetStartedFormPage(resumeSignal)
      },
      path("newsletter") {
        NewsletterPage()
      },
      noneMatched {
        ErrorPage(
          404,
          "Please check the URL in the address bar and try again. We can't find the page you requested."
        )
      }
    )

  lazy val appEventBus: EventBus[Event.RoutingEvent] =
    new EventBus[Event.RoutingEvent]

  lazy val resumeSignal: Var[Option[File]] = Var(None)

end State
