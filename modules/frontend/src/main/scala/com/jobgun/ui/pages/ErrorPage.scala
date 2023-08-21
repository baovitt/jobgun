package com.jobgun.ui.pages

import com.raquo.laminar.api.L.*

import com.jobgun.ui.Event.{RoutingEvent, ErrorPageEvent}
import com.raquo.laminar.modifiers.RenderableNode

import frontroute.*

object ErrorPage:

  def apply(code: Int, message: String): HtmlElement =
    div(
      cls := "relative justify-center h-screen max-h-full overflow-hidden lg:px-0 md:px-12",
      div(
        cls := "justify-center w-full text-center lg:p-10 max-auto",
        div(
          cls := "justify-center w-full mx-auto",
          p(
            cls := "text-5xl tracking-tight text-black lg:text-9xl",
            code
          ),
          p(
            cls := "max-w-xl mx-auto mt-4 text-lg tracking-tight text-gray-400",
            message
          )
        ),
        div(
          cls := "flex justify-center gap-3 mt-10",
          a(
            cls := "items-center justify-center w-full px-6 py-2.5 text-center text-white duration-200 bg-black border-2 border-black rounded-full inline-flex hover:bg-transparent hover:border-black hover:text-black focus:outline-none lg:w-auto focus-visible:outline-black text-sm focus-visible:ring-black",
            href := "/",
            "Home"
          )
        )
      )
    )

end ErrorPage
