package com.jobgun.ui

import com.raquo.laminar.api.L.*
import org.scalajs.dom
import frontroute.*

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

import zio.*

@js.native
@JSImport("stylesheets/main.css", JSImport.Namespace)
object Css extends js.Any

object App:
  val css: Css.type = Css

  @main def main =
    renderOnDomContentLoaded(
      dom.document.querySelector("#app"),
      Routes.root.amend(LinkHandler.bind)
    )
end App
