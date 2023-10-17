package com.jobgun.ui

import com.raquo.laminar.api.L.*

import com.jobgun.ui.pages.*
import com.jobgun.ui.domain.GlobalState
import com.jobgun.shared.domain.requests.JobRequest
import com.jobgun.shared.domain.responses.JobResponse
import com.jobgun.shared.domain.JobListing
import com.jobgun.ui.domain.requests.JobSearchWithResumeRequest
import com.jobgun.shared.domain.responses.JobResponse.JobSearchFromResumeResponse
import zio.json.DecoderOps

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

      case Event.ResumeEvent.StartResumeRequest() => (state: GlobalState) => 
        println("StartResumeRequest")
        state.copy(resumeRequestInFlight = true)

      case Event.ResumeEvent.UpdateEmbeddingFromResume(Right(response)) => (state: GlobalState) =>
        println("UpdateEmbeddingFromResume")
        state.copy(embedding = Some(response.embedding), jobListings = response.listings, resumeRequestInFlight = false)

      case e @ Event.ResumeEvent.UpdateEmbeddingFromResume(_) => (state: GlobalState) =>
        println("failed to resolve event" + e.toString)
        state
        
      case Event.ResumeEvent.RemoveResume =>
        (state: GlobalState) => GlobalState(None, None, List.empty)

      case _ => (state: GlobalState) => state
    }

  // State
  given currentGlobalState: Signal[GlobalState] =
    changerBus.foldLeft(GlobalState(None, None, List.empty)) {
      (currentLoginData, nextChanger) => nextChanger(currentLoginData)
    }
end State
