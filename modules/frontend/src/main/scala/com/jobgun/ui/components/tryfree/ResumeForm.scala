package com.jobgun.ui.components.tryfree

import com.raquo.laminar.api.L.*
import sttp.tapir.TapirFile
import zio.json.DecoderOps

import com.jobgun.ui.components.FileUploader
import com.jobgun.shared.domain.JobListing
import com.jobgun.shared.domain.requests.JobRequest.JobSearchWithResumeRequest
import com.jobgun.shared.domain.responses.JobResponse.JobSearchFromResumeResponse

import com.jobgun.ui.Event
import com.jobgun.ui.domain.GlobalState

import scala.concurrent.ExecutionContext.Implicits.global



import frontroute.*

object ResumeForm:

  def apply()(using eventBus: EventBus[Event], globalState: Signal[GlobalState]): HtmlElement =
    div(
      p(
        cls := "mt-5 text-base text-gray-500",
        "Upload your resume and we'll extract the information we need."
      ),
      br(),
      FileUploader(25, "PDF", "WORD"),
      child <-- globalState.map {
        case GlobalState(None, _, _, _) =>
          div()
        case GlobalState(Some(resume), _, _, resumeRequestInFlight) =>
          div(
            cls := "flex flex-col max-w-xl gap-3 mx-auto mt-10 lg:flex-row",
            button(
              cls := "items-center justify-center w-full px-6 py-2.5 text-center text-white duration-200 bg-black border-2 border-black rounded-full nline-flex hover:bg-transparent hover:border-black hover:text-black focus:outline-none lg:w-auto focus-visible:outline-black text-sm focus-visible:ring-black",
              onClick.flatMap { _ => 
                eventBus.emit(Event.ResumeEvent.StartResumeRequest(resume)) 

                val encodeRequest = (r: JobSearchWithResumeRequest) => {
                    val formData = new org.scalajs.dom.FormData()
                    formData.append("file", resume, "experienced-network-engineer-resume-example.pdf")
                    formData
                }

                val decodeResponse = (r: org.scalajs.dom.Response) => 
                  EventStream.fromJsPromise(r.text()).map(_.fromJson[JobSearchFromResumeResponse])

                FetchStream
                  .withCodec(encodeRequest, decodeResponse)
                  .post(
                    "http://localhost:5000/api/v1/jobs/resume",
                    _.headers(
                      "accept" -> "application/json",
                      "Content-Type" -> "multipart/form-data"
                    )
                  )

                // val formData = new org.scalajs.dom.FormData()
                // formData.append("file", resume, "experienced-network-engineer-resume-example.pdf")

                // // Create headers and append 'accept' header
                // val headers1 = new org.scalajs.dom.Headers()
                // headers1.append("accept", "application/json")

                // Make the POST request with multipart/form-data
                // val fetchOptions = new org.scalajs.dom.experimental.RequestInit {
                //   method = org.scalajs.dom.experimental.HttpMethod.POST
                //   body = formData
                //   headers = headers1
                // }
                // import org.scalajs.dom.experimental.Fetch._
                // import org.scalajs.dom.{ Response, WindowOrWorkerGlobalScope }



                // val fetchFuture = fetch("http://local.dev/api/v1/jobs/resume", fetchOptions)

                // for {
                //   response <- 
                //     println("fetchFuture")
                //     EventStream.fromJsPromise(fetchFuture)
                //   text <- 
                //     println(response)
                //     EventStream.fromJsPromise(response.text())
                // } yield {
                //   println(text.fromJson[JobSearchFromResumeResponse])
                //   text.fromJson[JobSearchFromResumeResponse]
                // }
              } --> { response => 
                eventBus.emit(Event.ResumeEvent.UpdateEmbeddingFromResume(response))
              },
              "Next"
            ),
            child <-- globalState.map { state =>
              if state.resumeRequestInFlight then
                div(
                  cls := "bg-gray-200 w-full min-h-screen flex justify-center items-center",
                  div(cls := "flex min-h-screen w-full items-center justify-center bg-gray-200",
                    div(cls := "flex h-14 w-14 items-center justify-center rounded-full bg-gradient-to-tr from-indigo-500 to-pink-500 animate-spin",
                      div(cls := "h-9 w-9 rounded-full bg-gray-200")
                    )
                  )
                )
              else
                div()
            },
            div(cls := "animate-spin inline-block w-6 h-6 border-[3px] border-current border-t-transparent text-gray-400 rounded-full", role := "status", dataAttr("aria-label") := "loading",
                  span(cls := "sr-only","Loading...")
                )
          )
      },
      // div(
      //   cls := "flex flex-col max-w-xl gap-3 mx-auto mt-10 lg:flex-row",
      //   button(
      //     cls := "items-center justify-center w-full px-6 py-2.5 text-center text-white duration-200 bg-black border-2 border-black rounded-full nline-flex hover:bg-transparent hover:border-black hover:text-black focus:outline-none lg:w-auto focus-visible:outline-black text-sm focus-visible:ring-black",
      //     "Next"
      //   )
      // )
    )

end ResumeForm
