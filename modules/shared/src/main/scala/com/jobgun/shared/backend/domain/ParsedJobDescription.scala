package com.jobgun.shared.backend

package domain

import zio.Chunk
import zio.json.{jsonField, JsonCodec}

final case class ParsedJobDescription private (
    @jsonField("required/preferred certifications") certifications: Chunk[
      String
    ],
    @jsonField("required/preferred education") education: Chunk[String],
    @jsonField("required/preferred experience") experience: Chunk[String],
    @jsonField("required/preferred licenses") licenses: Chunk[String],
    @jsonField(
      "required/preferred security credentials"
    ) securityCredentials: Chunk[String],
    @jsonField("required/preferred misc requirements") misc: Chunk[String]
) derives JsonCodec

object ParsedJobDescription:

  val defaultDevops = ParsedJobDescription(
    Chunk(),
    Chunk(
      "Associate of Applied Science — Information and Communication Technology from Resume Worded University, Location 2011"
    ),
    Chunk(
      "AWS DevOps Engineer at Resume Worded, Location from 2015 - 2023-11-17",
      "Assistant AWS DevOps Engineer at Growthsi, Location from 2013 - 2015",
      "DevOps Engineer (Intern) at Resume Worded Exciting Company, Location from 2011 - 2013"
    ),
    Chunk(),
    Chunk(),
    Chunk(
      "Security Auditing",
      "Dependency Scanning",
      "Static Code Analysis",
      "Continuous Integration",
      "Puppet Scripting",
      "English (Native)",
      "German (Fluent)",
      "French (Conversational)"
    )
  )

  val defaultConstructionManager = ParsedJobDescription(
    Chunk(),
    Chunk("High school diploma,Ballard High School, 2002 - 2006, Seattle, WA"),
    Chunk(
      "Construction Manager, AECOM, 2012 - 2023-11-17, Seattle, WA",
      "Construction Manager, HDR, 2008 - 2012, Seattle, WA",
      "Construction Worker, Prestige Plumbing & Heating, 2006 - 2008, Seattle, WA"
    ),
    Chunk(),
    Chunk(),
    Chunk(
      "Carpentry",
      "Remodeling",
      "Equipment Maintenance",
      "Power Tools",
      "Professionalism",
      "Multi-tasking"
    )
  )

  val defaultNetworkEngineer = ParsedJobDescription(
    Chunk("Cisco - CCNA", "Microsoft - MCITP"),
    Chunk(
      "Bachelor of Science, Information Science, University of Pittsburgh, September 2008 - April 2012, Pittsburgh, PA"
    ),
    Chunk(
      "Senior Network Engineer, Geller & Company, January 2015 - 2023-11-17, New York, NY",
      "Network Engineer, Audible, April 2012 - January 2015, Washington, DC"
    ),
    Chunk(),
    Chunk(),
    Chunk(
      "LAN/ WAN",
      "TCP/ IP Networking",
      "Cisco NEXUS / ISE / Prime (WiFi)",
      "FortiManager/ FortiGate",
      "Amazon EC2/ Direct Connect",
      "Routing protocols - BGP, OSPF, ECMP, MPLS",
      "Streamlined the process of troubleshooting and monitoring LAN/WAN activities",
      "Improved the response time to incoming incident response tickets by 15% through an automated process that assigns a ticket to qualified network engineers",
      "Built out technical documentation for processes and infrastructure, leading to a 2-day reduction in on-boarding for new engineers",
      "Led weekly “lunch and learn” sessions to instruct 15 other engineers on best practices around switching, routing, DNS, and IP addressing",
      "Worked with the leadership team to improve wired and wireless network infrastructure",
      "Drove operational improvements, increasing network efficiency and productivity by 11% year-over-year",
      "Led the expansion of the network into 10 new European countries",
      "Reviewed, audited, and on-boarded 25 external vendors into the network to handle billions of concurrent network requests",
      "Led a team of 2 full-time employees and 1 contractor,Implemented regular alerting and monitoring of network performance, which reduced network downtime by 11%",
      "Worked with 17 new clients to build robust firewalls using FortiManager and FortiGate"
    )
  )

  val defaultReactDeveloper = ParsedJobDescription(
    Chunk(),
    Chunk(
      "Associate of Applied Science — Information Technology from Resume Worded University, New York, NY 2011"
    ),
    Chunk(
      "React Front End Developer at Resume Worded, New York, NY from 2015 - 2023-11-17",
      "React.js Developer at Growthsi, San Francisco, CA from 2013 - 2015",
      "Web Developer at Resume Worded Exciting Company, San Francisco, CA from 2011 - 2013"
    ),
    Chunk(),
    Chunk(),
    Chunk(
      "Semantic HTML5",
      "JavaScript",
      "ReactJS",
      "CSS compiled using maven and webpack build tools",
      "Java OSGi bundles",
      "CI/CD solution using the CircleCI tool and AWS Lambda functions",
      "Isomorphic React and Node.js",
      "Java/JSP",
      "AngularJS (Advanced)",
      "ReactJS (Experienced)",
      "BackboneJS",
      "MVC Architecture",
      "NodeJS",
      "English (Native)",
      "German (Fluent)",
      "French (Conversational)"
    )
  )
end ParsedJobDescription
