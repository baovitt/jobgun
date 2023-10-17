package com.jobgun.ui

import org.scalajs.dom.File

import com.jobgun.shared.domain.responses.JobResponse

sealed trait Event

object Event:
  /** Event that is emitted when the user navigates to a new page */
  sealed trait RoutingEvent(location: String) extends Event:
    def getLocation: String = location
  end RoutingEvent

  /** Event that is emitted when the user clicks on a component */
  sealed trait ClickEvent extends Event

  /** Event that is emitted when the user submits a form */
  sealed trait SubmitEvent extends Event

  /** Event that is emitted when the user hovers over a component */
  sealed trait HoverEvent extends Event

  /** Event that is emitted when the user scrolls */
  sealed trait ScrollEvent extends Event

  /** Event that is emitted when the user resizes the window */
  sealed trait ResizeEvent extends Event

  /** Event that is emitted when the user drops an element */
  sealed trait DropEvent extends Event

  /** Event that is emitted when the user changes the value of an input */
  sealed trait InputEvent extends Event

  enum ErrorPageEvent extends Event:
    case RouteHome
        extends ErrorPageEvent
        with RoutingEvent(location = "/")
        with ClickEvent
  end ErrorPageEvent

  enum FrontNavbarEvent extends Event:
    case RouteHome
        extends FrontNavbarEvent
        with RoutingEvent(location = "/")
        with ClickEvent
    case RouteGetStarted
        extends FrontNavbarEvent
        with RoutingEvent(location = "/get-started")
        with ClickEvent
    case RouteAbout
        extends FrontNavbarEvent
        with RoutingEvent(location = "/about")
        with ClickEvent
    case RouteNewsletter
        extends FrontNavbarEvent
        with RoutingEvent(location = "/newsletter")
        with ClickEvent
  end FrontNavbarEvent

  enum LandingPageEvent:
    case RouteGetStarted
        extends LandingPageEvent
        with RoutingEvent(location = "/get-started")
        with ClickEvent
    case RouteLearnMore
        extends LandingPageEvent
        with RoutingEvent(location = "/about")
        with ClickEvent
  end LandingPageEvent

  enum ResumeEvent extends Event:
    // Resume is uploaded through the uploader
    case AddResume(resume: File)
        extends ResumeEvent
        with InputEvent
    // signal to start the loading animation and make the web request to the backend
    case StartResumeRequest()
        extends ResumeEvent
        with InputEvent
    // signal to stop the loading animation and display the results
    case UpdateEmbeddingFromResume(response: Either[String, JobResponse.JobSearchFromResumeResponse])
        extends ResumeEvent
        with RoutingEvent(location = "/jobs")
    case RemoveResume
        extends ResumeEvent
        with RoutingEvent(location = "/get-started")
        with ClickEvent
  end ResumeEvent
end Event
