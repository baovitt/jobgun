package com.jobgun.ui.components

import com.raquo.laminar.api.L.*
import org.scalajs.dom.{File, HTMLInputElement, DataTransferDropEffectKind}

import frontroute.*

object FileUploader:

  def noFileMessage(maxFileSize: Int, fileExtensions: String*) =
    div(
        cls := "flex flex-col items-center justify-center pt-5 pb-6",
        svg.svg(
            svg.cls := "w-10 h-10 mb-3 text-gray-400",
            svg.fill := "none",
            svg.stroke := "currentColor",
            svg.viewBox := "0 0 24 24",
            svg.xmlns := "http://www.w3.org/2000/svg",
            svg.path(
                svg.strokeLineCap := "round",
                svg.strokeLineJoin := "round",
                svg.strokeWidth := "2",
                svg.d := "M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"
            )
        ),
        p(
            cls := "mb-2 text-sm text-gray-500 dark:text-gray-400",
            span(
                cls := "font-semibold",
                "Click to upload"
            ),
        ),
        p(
            cls := "text-xs text-gray-500 dark:text-gray-400",
            s"${fileExtensions.mkString(", ")} (Max $maxFileSize MB)"
        )
    )

  def wrongFileTypeMessage(fileExtensions: String*) =
    div(
        cls := "flex flex-col items-center justify-center pt-5 pb-6",
        svg.svg(
            svg.cls := "w-10 h-10 mb-3 text-red-400",
            svg.fill := "none",
            svg.stroke := "currentColor",
            svg.viewBox := "0 0 24 24",
            svg.xmlns := "http://www.w3.org/2000/svg",
            svg.path(
                svg.strokeLineCap := "round",
                svg.strokeLineJoin := "round",
                svg.strokeWidth := "2",
                svg.d := "M6 18L18 6M6 6l12 12"
            )
        ),
        p(
            cls := "mb-2 text-sm text-red-500 dark:text-red-400",
            span(
                cls := "font-semibold",
                "Wrong file type"
            ),
            s" (only ${fileExtensions.mkString(", ")})"
        ),
        p(
            cls := "text-xs text-gray-500 dark:text-gray-400",
            "Please upload a valid file or fill manually"
        )
    )

  def fileSizeTooBigMessage = 
    div(
        cls := "flex flex-col items-center justify-center pt-5 pb-6",
        // svg of a complex bloated cartoonish file
        svg.svg(
            svg.cls := "w-10 h-10 mb-3 text-red-400",
            svg.fill := "none",
            svg.stroke := "currentColor",
            svg.viewBox := "0 0 24 24",
            svg.xmlns := "http://www.w3.org/2000/svg",
            svg.path(
                svg.strokeLineCap := "round",
                svg.strokeLineJoin := "round",
                svg.strokeWidth := "2",
                svg.d := "M12 2L2 7v10a2 2 0 002 2h16a2 2 0 002-2V7l-10-5zm0 0v10m0 0l-2-2m2 2l2-2"
            )
        ),
        p(
            cls := "mb-2 text-sm text-red-500 dark:text-red-400",
            span(
                cls := "font-semibold",
                "File size too big"
            )
        ),
        p(
            cls := "text-xs text-gray-500 dark:text-gray-400",
            "Please upload a file smaller than 25 MB"
        )
    )

  def fileUploadedMessage(resumeFile: Var[Option[File]]) = 
    div(
        cls := "flex flex-col items-center justify-center pt-5 pb-6",
        svg.svg(
            svg.cls := "w-10 h-10 mb-3 text-green-400",
            svg.fill := "none",
            svg.stroke := "currentColor",
            svg.viewBox := "0 0 24 24",
            svg.xmlns := "http://www.w3.org/2000/svg",
            svg.path(
                svg.strokeLineCap := "round",
                svg.strokeLineJoin := "round",
                svg.strokeWidth := "2",
                svg.d := "M5 13l4 4L19 7"
            )
        ),
        p(
            cls := "mb-2 text-sm text-green-500 dark:text-green-400",
            span(
                cls := "font-semibold",
                "File uploaded"
            )
        ),
        p(
            cls := "text-xs text-gray-500 dark:text-gray-400 text-center",
            "Click again to reupload."
        )
    )

  def justifyMessage(maxFileSize: Int, resumeFile: Var[Option[File]], fileExtensions: String*) =
    resumeFile.signal.map {
        case Some(file) => 
            if (file.size > maxFileSize * 1024 * 1024) fileSizeTooBigMessage
            else if (fileExtensions.map(e => s".${e.toLowerCase}").forall(e => !file.name.endsWith(e))) wrongFileTypeMessage("PDF", "WORD")
            else fileUploadedMessage(resumeFile)
        case None => noFileMessage(25, "PDF", "WORD")
    }

  def apply(
      resumeFile: Var[Option[File]],
      maxFileSize: Int,
      fileExtensions: String*
  ) =
    div(
      cls := "flex items-center justify-center w-full pr-5",
      label(
        cls := "flex flex-col items-center justify-center w-full h-64 border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50 dark:hover:bg-bray-800 dark:bg-gray-700 hover:bg-gray-100 dark:border-gray-600 dark:hover:border-gray-500 dark:hover:bg-gray-600",
        child <-- justifyMessage(maxFileSize, resumeFile, fileExtensions*),
        input(
          `type` := "file",
          cls := "hidden",
          accept := fileExtensions.map(e => s".${e.toLowerCase}").mkString(", "),
          onChange --> (
            _.target match
              case fileInputElement: HTMLInputElement =>
                if (fileInputElement.files.length == 0) ()
                else resumeFile.set(Some(fileInputElement.files(0)))
              case e => println(s"Unexpected event target: ${e}") 
          ),
        )
      )
    )
end FileUploader