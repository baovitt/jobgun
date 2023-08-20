package com.jobgun.ui

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
    case RouteTryFree
        extends FrontNavbarEvent
        with RoutingEvent(location = "/try-free")
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
        with RoutingEvent(location = "/try-free")
        with ClickEvent
    case RouteLearnMore
        extends LandingPageEvent
        with RoutingEvent(location = "/about")
        with ClickEvent
  end LandingPageEvent
end Event