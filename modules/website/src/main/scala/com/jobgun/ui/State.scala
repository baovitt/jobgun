package com.jobgun.ui

import com.raquo.laminar.api.L.*

import com.jobgun.ui.pages.*
import com.jobgun.ui.domain.GlobalState
import com.jobgun.shared.domain.requests.JobRequest
import com.jobgun.shared.domain.responses.JobResponse

import sttp.model.StatusCode

import zio.*

object State:

  // EventStreams
  given appEventBus: EventBus[Event] = new EventBus[Event]
  val changerBus: EventStream[GlobalState => GlobalState] =
    appEventBus.events.collect {
      case Event.ResumeEvent.AddResume(resume) => (state: GlobalState) => 
        println("AddResume")
        state.copy(resume = Some(resume))

      case Event.ResumeEvent.StartResumeRequest(resume) => (state: GlobalState) => 
        println("StartResumeRequest")
        state.copy(resumeRequestInFlight = true)

      case Event.ResumeEvent.UpdateEmbeddingFromResume(Right(response)) => (state: GlobalState) =>
        println("UpdateEmbeddingFromResume")
        state
          .copy(embedding = Some(response.embedding))
          .copy(jobListings = response.listings)

      case Event.ResumeEvent.RemoveResume =>
        (state: GlobalState) => GlobalState(None, None, List.empty)

      case _ => 
        (state: GlobalState) => GlobalState(None, None, List.empty)
    }

  // State
  given currentGlobalState: Signal[GlobalState] =
    changerBus.foldLeft(GlobalState(None, None, List.empty)) {
      (currentLoginData, nextChanger) => nextChanger(currentLoginData)
    }
end State
