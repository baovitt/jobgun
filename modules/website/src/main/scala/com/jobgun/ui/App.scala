package com.jobgun.ui

import State.root
import com.raquo.laminar.api.L.*
import org.scalajs.dom
import frontroute.*

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("stylesheets/main.css", JSImport.Namespace)
object Css extends js.Any

object App:
  val css: Css.type = Css

  @main def main: Unit =
    val _ = renderOnDomContentLoaded(
      dom.document.querySelector("#app"),
      root.amend(LinkHandler.bind)
    )
end App
