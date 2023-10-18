package com.jobgun.ui.components.tryfree

import com.raquo.laminar.api.L.*
import com.jobgun.ui.components.FileUploader
import com.jobgun.shared.domain.JobListing
import com.jobgun.ui.domain.requests.JobSearchWithResumeRequest
import com.jobgun.shared.domain.responses.JobResponse.JobSearchFromResumeResponse
import com.jobgun.ui.Event
import com.jobgun.ui.domain.GlobalState
import org.scalajs.dom.File
import zio.json.DecoderOps
import scala.concurrent.ExecutionContext.Implicits.global

object ResumeForm:

  private def fetchJobsFromResume(
      resume: File
  )(using eventBus: EventBus[Event]): EventStream[Event.ResumeEvent] = {
    val encodeRequest = (r: JobSearchWithResumeRequest) => {
      val formData = new org.scalajs.dom.FormData()
      formData.append(
        "file",
        resume
      )
      formData
    }

    val decodeResponse = (r: org.scalajs.dom.Response) =>
      EventStream
        .fromJsPromise(r.text())
        .map(_.fromJson[JobSearchFromResumeResponse])

    FetchStream
      .withCodec(encodeRequest, decodeResponse)
      .post(
        "http://localhost:5000/api/v1/jobs/resume",
        _.headers("accept" -> "application/json"),
        _.body(JobSearchWithResumeRequest(resume))
      )
      .map(Event.ResumeEvent.UpdateEmbeddingFromResume.apply)
  }

  def apply(hitNext: Var[Boolean])(using
      eventBus: EventBus[Event],
      globalState: Signal[GlobalState]
  ): HtmlElement =
    div(
      p(
        cls := "mt-5 text-base text-gray-500",
        "Upload your resume and we'll extract the information we need."
      ),
      br(),
      FileUploader(25, "PDF", "WORD"),
      div(
        cls := "flex flex-col max-w-xl gap-3 mx-auto mt-10 lg:flex-row",
        button(
          cls <-- globalState.map {
            case GlobalState(None, _, _, _) => "hidden"
            case _ =>
              "items-center justify-center w-full px-6 py-2.5 text-center text-white duration-200 bg-black border-2 border-black rounded-full nline-flex hover:bg-transparent hover:border-black hover:text-black focus:outline-none lg:w-auto focus-visible:outline-black text-sm focus-visible:ring-black"
          },
          onClick.preventDefault
            .compose(
              _.withCurrentValueOf(globalState)
                .collect { case (ev, GlobalState(Some(resume), _, _, false)) =>
                  hitNext.set(true)
                  eventBus.emit(Event.ResumeEvent.StartResumeRequest())
                  fetchJobsFromResume(resume)
                }
                .flatten
            ) --> eventBus.writer,
          "Next"
        )
      )
    )

end ResumeForm
