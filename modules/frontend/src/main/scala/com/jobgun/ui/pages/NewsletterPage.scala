package com.jobgun.ui.pages

import com.raquo.laminar.api.L.*
import com.jobgun.ui.components.FrontNavbar
import frontroute.*

object NewsletterPage:
  def apply() =
    div(
      FrontNavbar(),
      div(
        cls := "py-24 bg-white",
        div(
          cls := "relative px-8",
          div(
            cls := "max-w-3xl text-center lg:text-left",
            div(
              cls := "max-w-xl mx-auto text-center lg:p-10 lg:text-left",
              div(
                p(
                  cls := "text-2xl font-medium tracking-tight text-black sm:text-4xl",
                  "Subscribe to our Newsletter"
                ),
                p(
                  cls := "max-w-xl mt-4 text-base tracking-tight text-gray-600",
                  """Subscribe to our newsletter to stay up to date on our latest
                                            products and services."""
                )
              ),
              br(),
              br(),
              label(
                cls := "block mb-3 text-sm font-medium text-gray-600",
                "How shall we contact you?"
              ),
              input(
                cls := "block w-full px-6 py-3 text-black bg-white border border-gray-200 appearance-none rounded-xl placeholder:text-gray-400 focus:border-blue-500 focus:outline-none focus:ring-blue-500 sm:text-sm",
                placeholder := "email@example.com",
                `type` := "email"
              ),
              br(),
              button(
                cls := "items-center justify-center px-6 py-2.5 text-center text-white duration-200 bg-black border-2 border-black rounded-full nline-flex hover:bg-transparent hover:border-black hover:text-black focus:outline-none focus-visible:outline-black text-sm focus-visible:ring-black",
                `type` := "submit",
                "Subscribe"
              )
            )
          )
        )
      )
    )
end NewsletterPage
