package com.jobgun.ui.components

import com.raquo.laminar.api.L.*

import frontroute.*

object FrontNavbar:

  def apply(): HtmlElement =
    lazy val open: Var[Boolean] = Var(false)

    div(
      cls := "w-full mx-auto bg-white border-b 2xl:max-w-7xl",
      div(
        cls := "relative flex flex-col w-full p-5 mx-auto bg-white md:items-center md:justify-between md:flex-row md:px-6 lg:px-8",
        div(
          cls := "flex flex-row items-center justify-between lg:justify-start",
          a(
            cls := "text-lg tracking-tight text-black uppercase focus:outline-none focus:ring lg:text-2xl",
            href := "/",
            span(
              cls := "lg:text-lg uppecase focus:ring-0",
              "Jobgun"
            )
          ),
          button(
            onClick --> (_ => open.update(!_)),
            cls := "inline-flex items-center justify-center p-2 text-gray-400 hover:text-black focus:outline-none focus:text-black md:hidden",
            svg.svg(
              svg.cls := "w-6 h-6",
              svg.stroke := "currentColor",
              svg.fill := "none",
              svg.viewBox := "0 0 24 24",
              svg.path(
                svg.cls.toggle("hidden") <-- open,
                svg.cls.toggle("inline-flex") <-- open.signal.map(!_),
                svg.cls := "inline-flex",
                svg.strokeLineCap := "round",
                svg.strokeLineJoin := "round",
                svg.strokeWidth := "2",
                svg.d := "M4 6h16M4 12h16M4 18h16"
              ),
              svg.path(
                svg.cls.toggle("inline-flex") <-- open,
                svg.cls.toggle("hidden") <-- open.signal.map(!_),
                svg.strokeLineCap := "round",
                svg.strokeLineJoin := "round",
                svg.strokeWidth := "2",
                svg.d := "M6 18L18 6M6 6l12 12"
              )
            )
          )
        ),
        navTag(
          cls.toggle("flex") <-- open,
          cls.toggle("hidden") <-- open.signal.map(!_),
          cls := "flex-col flex-grow hidden py-10 md:flex lg:py-0 md:justify-end md:flex-row",
          ul(
            cls := "space-y-2 list-none md:space-y-0 md:items-center md:inline-flex",
            li(
              extractMatchedPath.signal { path =>
                a(
                  href := "/",
                  cls := "px-2 py-8 text-sm text-gray-500 border-b-2 border-transparent lg:px-6 md:px-3 hover:text-blue-600",
                  "HOME"
                ).amend(
                  cls.toggle("line-through") <-- path.map(_.mkString("/") == "")
                )
              }
            ),
            li(
              extractMatchedPath.signal { path =>
                a(
                  href := "/get-started",
                  cls := "px-2 py-8 text-sm text-gray-500 border-b-2 border-transparent lg:px-6 md:px-3 hover:text-blue-600",
                  "GET STARTED"
                ).amend(
                  cls.toggle("line-through") <-- path.map(_.mkString("") == "get-started")
                )
              }
            ),
            li(
              extractMatchedPath.signal { path =>
                a(
                  href := "/about",
                  cls := "px-2 py-8 text-sm text-gray-500 border-b-2 border-transparent lg:px-6 md:px-3 hover:text-blue-600",
                  "ABOUT"
                ).amend(
                  cls.toggle("line-through") <-- path.map(_.mkString("") == "about")
                )
              }
            ),
            li(
              extractMatchedPath.signal { path =>
                a(
                  href := "/newsletter",
                  cls := "px-2 py-8 text-sm text-gray-500 border-b-2 border-transparent lg:px-6 md:px-3 hover:text-blue-600",
                  "NEWSLETTER"
                ).amend(
                  cls.toggle("line-through") <-- path.map(_.mkString("") == "newsletter")
                )
              }
            )
          )
        )
      )
    )

end FrontNavbar