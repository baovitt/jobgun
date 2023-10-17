package com.jobgun.ui

import com.raquo.laminar.api.L._
import frontroute.*
import com.jobgun.ui.pages.*
import State.{given, *}

object Routes:
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
        GetStartedFormPage()
      },
      path("newsletter") {
        NewsletterPage()
      },
      path("jobs") {
        JobsPage()
      },
      noneMatched {
        ErrorPage(
          404,
          "Please check the URL in the address bar and try again. We can't find the page you requested."
        )
      }
    )
end Routes
