package com.jobgun.ui.pages

import com.raquo.laminar.api.L.*

import com.jobgun.ui.Event.{RoutingEvent, LandingPageEvent}
import com.raquo.laminar.modifiers.RenderableNode

import com.jobgun.ui.components.FrontNavbar
// import frontroute.*

import com.raquo.laminar.nodes.TextNode

import com.jobgun.ui.components.jobs.JobListing

object JobsPage:
  private def jobMatchColor(jobMatchPercent: Int) =
    val p: Double = jobMatchPercent / 100.0
    s"rgb(${((1.0 - p) * 178).toInt}, ${(p * 178).toInt}, 0)"

  def apply() =
    div(
      FrontNavbar(),
      div(
        cls := "items-center w-full px-5 py-24 mx-auto md:px-12 lg:px-16 max-w-7xl",
        div(
          cls := "space-y-12 divide-y-2 divide-mercury-300",
          JobListing(
            "Network Engineer",
            "Evil Corp, LLC",
            "We are looking for an exceptional Network Engineer to join our multi-tenant hosting team. You'll be instrumental in deploying, operating, and scaling a high-performance network that provides world-class availability, performance, and security. With this position comes a host (pun intended) of interesting and challenging issues that will test your technical prowess and call for clever, well-designed solutions.\n\nYou’ll provide network support within a small, collaborative team that is changing the way some of the nation’s largest healthcare organizations access our software. You’ll keep abreast of the latest technologies in a hands-on role that also provides a unique opportunity to shape the future direction and design of our systems while working directly with our customers through issues that arise. The ideal candidate is passionate about technology and the opportunity to play a foundational role in a new team, is self-motivated, and has great communication skills. Learn more about the team at https //careers.evilcorp.com/Jobs/Hosting.\n\nAs a member of the Hosting team at Evil Corp you'll be responsible for the confidentiality, integrity, and availability of Epic Hosting. Those responsibilities include:\n\n• Following policies and procedures and escalating when policies or procedures are not followed\n• Escalating potential security incidents and providing relevant information\n• Meeting all security responsibilities defined in policies and procedures\n\n\nMore than just important work\n\nEpic is located in Madison, Wisconsin, a city regularly ranked as one of America's best places to live. Epic's environment is one of continuous learning; you'll have access to opportunities to expand your skill set and share your knowledge with others. We offer comprehensive benefits to keep you healthy and happy as you grow in your life and career, and your merit-based compensation will reflect the impact your work has on the company and our customers. You'll also be eligible for annual raises and bonuses, as well as stock grants, which give you an even greater stake in the success of Epic and our customers. Epic is an Equal Opportunity employer. We seek diverse perspectives, backgrounds, and experiences in our mission to improve healthcare. Research has shown that job-seekers who are women, LGBTQ+, or members of the global majority are less likely to apply for roles they don't seem completely qualified for, so we encourage all who are interested to apply. Please see our full non-discrimination statement at https //careers.epic.com/EEO.\n\nThis position features three different starting points, based upon prior experience.\n\nJunior Network Engineer requires 0-2 years of hands-on experience\n\nNetwork Engineer requires 2-5 years of hands-on experience\n\nSenior Network Engineer requires 5+ years of hands-on experience\n\nRequired Qualifications\n\n• Relocation to the Madison, WI area (reimbursed)\n• Routing protocol knowledge and experience, specifically OSPF and BGP\n• Strong network troubleshooting background\n• Eligibility to work in the U.S. without visa sponsorship\n• COVID-19 vaccination\n\n\nPreferred Qualifications\n\n• CCNA preferred\n• Supporting multi-tenant infrastructure\n• IPSec VPN or other encryption knowledge and experience\n• Routing technology knowledge of MPLS, IS-IS, Segment Routing\n• Experience developing integrations with network solutions with automation tools (e.g. Ansible)",
            98,
            "Remote",
            "110k - 130k",
            "Full Time",
            "https://www.linkedin.com/jobs/view/network-engineer-at-epic-2660251271"
          )
        )
      ),
      div(
        cls := "items-center px-8 mx-auto max-w-7xl lg:px-16 md:px-12",
        div(
          cls := "justify-center w-full lg:p-10 max-auto",
          div(
            cls := "flex items-center justify-center px-4 py-3 bg-white sm:px-6",
            div(
              cls := "flex justify-center flex-1 gap-3",
              a(
                href := "#",
                cls := "inline-flex items-center justify-center p-3 text-sm text-gray-700 rounded-xl group ring-1 focus:outline-none ring-gray-200 hover:text-black hover:ring-blue-300 active:bg-gray-50 focus-visible:outline-gray-600 focus-visible:ring-gray-300",
                "First"
              ),
              a(
                href := "#",
                cls := "inline-flex items-center justify-center p-3 text-sm text-gray-700 rounded-xl group ring-1 focus:outline-none ring-gray-200 hover:text-black hover:ring-blue-300 active:bg-gray-50 focus-visible:outline-gray-600 focus-visible:ring-gray-300",
                "Previous"
              ),
              a(
                href := "#",
                cls := "inline-flex items-center justify-center p-3 text-sm text-gray-700 rounded-xl group ring-1 focus:outline-none ring-gray-200 hover:text-black hover:ring-blue-300 active:bg-gray-50 focus-visible:outline-gray-600 focus-visible:ring-gray-300",
                "Next"
              )
            )
          )
        )
      )
    )
end JobsPage
