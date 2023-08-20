package com.jobgun.ui.components.tryfree

import com.raquo.laminar.api.L.*
import org.scalajs.dom.File

import com.jobgun.ui.components.FileUploader

import frontroute.*

object ResumeForm:

  def apply(resumeFile: Var[Option[File]]): HtmlElement =
    div(
      p(
        cls := "mt-5 text-base text-gray-500",
        """Upload your resume and we'll extract the information we need. If
                        you don't have an updated resume, hit next below."""
      ),
      br(),
      FileUploader(resumeFile, 25, "PDF", "WORD"),
      div(
        cls := "flex flex-col max-w-xl gap-3 mx-auto mt-10 lg:flex-row",
        button(
          cls := "items-center justify-center w-full px-6 py-2.5 text-center text-white duration-200 bg-black border-2 border-black rounded-full nline-flex hover:bg-transparent hover:border-black hover:text-black focus:outline-none lg:w-auto focus-visible:outline-black text-sm focus-visible:ring-black",
          "Next"
        )
      )
    )

end ResumeForm