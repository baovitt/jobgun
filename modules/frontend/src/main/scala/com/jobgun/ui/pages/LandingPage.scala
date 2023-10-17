package com.jobgun.ui.pages

import com.raquo.laminar.api.L.*

import com.jobgun.ui.Event.{RoutingEvent, LandingPageEvent}
import com.raquo.laminar.modifiers.RenderableNode

import com.jobgun.ui.components.FrontNavbar
import frontroute.*

object LandingPage:
  def apply() =
    div(
      FrontNavbar(),
      div(
        cls := "items-center px-8 py-12 mx-auto max-w-7xl lg:px-16 md:px-12 lg:py-24",
        div(
          cls := "justify-center w-full text-center lg:p-10 max-auto",
          div(
            cls := "justify-center w-full mx-auto",
            p(
              cls := "mt-8 text-5xl font-medium tracking-tighter text-black",
              "Introducing Jobgun"
            ),
            p(
              cls := "max-w-xl mx-auto mt-4 text-lg tracking-tight text-gray-600",
              "Search jobs faster with your resume"
            )
          ),
          div(
            cls := "flex flex-col items-center justify-center max-w-xl gap-3 mx-auto mt-10 lg:flex-row",
            a(
              href := "/get-started",
              cls := "items-center justify-center w-full px-6 py-2.5 text-center text-white duration-200 bg-black border-2 border-black rounded-full inline-flex hover:bg-transparent hover:border-black hover:text-black focus:outline-none lg:w-auto focus-visible:outline-black text-sm focus-visible:ring-black",
              "Get Started"
            ),
            a(
              href := "/about",
              cls := "inline-flex items-center justify-center text-sm font-semibold text-black duration-200 hover:text-blue-500 focus:outline-none focus-visible:outline-gray-600",
              "Learn more"
            )
          )
        )
      )
    )
end LandingPage
