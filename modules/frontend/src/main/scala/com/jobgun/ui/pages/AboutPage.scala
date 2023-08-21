package com.jobgun.ui.pages

import com.raquo.laminar.api.L.*
import com.jobgun.ui.components.FrontNavbar
import frontroute.*

object AboutPage:
  def apply() =
    div(
      FrontNavbar(),
      div(
        cls := "items-center w-full px-5 py-24 mx-auto md:px-12 lg:px-16 max-w-7xl",
        div(
          cls := "grid items-start grid-cols-1 md:grid-cols-2",
          div(
            cls := "lg:pr-24 md:pr-12",
            h2(
              cls := "mt-6 text-3xl font-medium text-black",
              "Jobgun - Advancing Job Search"
            ),
            p(
              cls := "mt-5 text-base text-gray-500",
              "Jobgun is a software product that harnesses the power of artificial intelligence to revolutionize job searching. Our platform goes beyond the basic keyword matching used by traditional job boards. Instead, Jobgun delves deeper to analyze job listings and understand the nuances of different roles. By doing this, Jobgun connects candidates with job opportunities that perfectly match their skillsets, qualifications, and aspirations."
            ),
            ul(
              cls := "grid grid-cols-2 gap-4 mt-12 list-none lg:gap-6",
              role := "list",
              li(
                div(
                  div(
                    cls := "flex items-center justify-center w-12 h-12 text-black bg-gray-100 rounded-xl",
                    "❖"
                  ),
                  p(
                    cls := "mt-5 text-lg font-medium leading-6 text-black",
                    "Global Database"
                  )
                ),
                div(
                  cls := "mt-2 text-base text-gray-500",
                  "Our global job database ensures that you have access to opportunities from around the world."
                )
              ),
              li(
                div(
                  div(
                    cls := "flex items-center justify-center w-12 h-12 text-black bg-gray-100 rounded-xl",
                    "❖"
                  ),
                  p(
                    cls := "mt-5 text-lg font-medium leading-6 text-black",
                    "Smart Matching"
                  )
                ),
                div(
                  cls := "mt-2 text-base text-gray-500",
                  "Our AI-driven algorithms ensure that the jobs you see are tailored to your unique qualifications and goals."
                )
              )
            )
          ),
          div(
            cls := "h-full mt-12 lg:mt-0 border-mercury-400 lg:pl-24 md:border-l md:pl-12",
            img(
              width := "4085",
              height := "4085",
              alt := "Lexingtøn thumbnail",
              cls := "object-cover bg-gray-300",
              src := "https://d33wubrfki0l68.cloudfront.net/ded521c426f480d4e473a11836c6ab8e7e948c84/95877/images/placeholders/square3.svg"
            )
          )
        )
      )
    )
end AboutPage
