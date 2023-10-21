package com.jobgun.ui.components.jobs

import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.TextNode

object JobListing:
  private def jobMatchColor(jobMatchPercent: Int) =
    val p: Double = jobMatchPercent / 100.0
    s"rgb(${((1.0 - p) * 178).toInt}, ${(p * 178).toInt}, 0)"

  def apply(
      title: String,
      company: String,
      description: String,
      percentMatch: Int,
      location: String,
      employmentType: String,
      url: String
  ) =
    val expanded: Var[Boolean] = Var(false)

    div(
      cls := "grid grid-cols-1 gap-4 pt-4 lg:grid-cols-3 lg:pt-12",
      div(
        cls := "flex flex-col flex-shrink-0 mb-6 lg:pr-12 md:mb-0",
        span(
          cls := s"text-base",
          styleAttr := s"color: ${jobMatchColor(percentMatch)}",
          s"$percentMatch%"
        ),
        span(
          cls := "text-lg font-medium leading-6 text-black",
          title
        ),
        span(
          cls := "text-base text-gray-500",
          company
        ),
        br(),
        span(
          cls := "text-base text-gray-500",
          "Location:"
        ),
        span(
          cls := "text-base text-gray-500 font-bold",
          location
        ),
        br(),
        span(
          cls := "text-base text-gray-500",
          "Employment Type:"
        ),
        span(
          cls := "text-base text-gray-500 font-bold",
          employmentType
        ),
        br(),
        a(
          href := url,
          target := "_blank",
          cls := "items-center justify-center w-full py-2.5 text-center text-white duration-200 bg-black border-2 border-black rounded-full inline-flex hover:bg-transparent hover:border-black hover:text-black focus:outline-none lg:w-auto focus-visible:outline-black text-sm focus-visible:ring-black",
          "Apply on Linkedin"
        )
      ),
      div(
        cls := "lg:col-span-2",
        div(
          cls <-- expanded.signal.map(e =>
            if e then "" else "h-64 overflow-hidden"
          ),
          p(
            cls := "text-base text-gray-500",
            description.split("\n").flatMap(l => List(TextNode(l), br()))
          )
        ),
        br(),
        div(
          cls := "underline",
          onClick --> (_ => expanded.update(!_)),
          child.text <-- expanded.signal.map(e =>
            if e then "See less" else "See more"
          )
        )
      )
    )
end JobListing
