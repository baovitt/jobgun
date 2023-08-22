package com.jobgun.ui.pages

import com.raquo.laminar.api.L.*

import com.jobgun.ui.Event.{RoutingEvent, LandingPageEvent}
import com.raquo.laminar.modifiers.RenderableNode

import com.jobgun.ui.components.FrontNavbar
import frontroute.*

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
            ),
          div(
            cls := "grid grid-cols-1 gap-4 pt-4 lg:grid-cols-3 lg:pt-12",
            div(
              cls := "flex flex-col flex-shrink-0 mb-6 lg:pr-12 md:mb-0",
              span(
                cls := "text-lg font-medium leading-6 text-black ",
                """100% REMOTE .NET or Java Full Stack Developer (React.js or Angular AWS)"""
              ),
              span(
                cls := "text-base text-gray-500",
                "OHIO Lottery"
              )
            ),
            div(
              cls := "space-y-4 lg:col-span-2",
              p(
                cls := "text-base text-gray-500",
                "100% REMOTE .NET / Java Full Stack Developer (React.js / Angular AWS) | Aerospace Defense (up to 18 mos to perm)\n \n***MUST have a U.S. CITIZENSHIP to work for the DoD***\n\n\nSUMMARY:\n• Plans, conducts, and coordinates software development activities.\n• Designs, develops, documents, tests, and debugs software that contains logical and mathematical solutions to business/mission problems or questions in computer language for solutions by means of data processing equipment.\n• Applies the appropriate standards, processes, procedures, and tools throughout the development life cycle.\n• Applies knowledge of computer hardware and software, subject matter to be programmed in business/mission applications, information processing techniques used, and information gathered from system users to develop software.\n• Corrects program errors, prepares operating instructions, compiles documentation of program development, and analyzes system capabilities to resolve questions of program intent, output requirements, input data acquisition, programming techniques, and controls.\n• Ensures software standards are met.\n• In addition to the typical Software Development Activities listed above, the selected candidate's primary focus will be to implement replacement assemblies in the online assembly system.\n \nREQUIREMENTS:\n• 4+ years of Mid-level to Sr. OOP/SOA experience creating client/server-side (full stack) C# .NET/.NET Core or Java/J2EE applications\n• 4+ years of responsive web UI experience creating Single Page Applications (SPAs) using JavaScript Frameworks: React.js or Angular (preferred), JavaScript, JQuery, HTML, BootStrap, CSS\n• Version control / Atlassian tools: Team Foundation Server (TFS), Git, GitHub, SVN, JIRA, Confluence, Bamboo, or BitBucket\n• Unit Testing: JUnit, JMeter, NHibernate\n• Full SDLC, Agile methdologies (either Scrum, Kanban, Rally, TDD), Waterfall, etc.\n\n\nHIGHLY DESIRED (but NOT required):\n• Will pick up any active DoD security clearances (highly preferred)\n• Familiar with with AWS/EC2 or S3 cloud deployment\n• Any Microsoft .NET developer or AWS certifications\n• Familiar with any CI/CD automation build or Configuration Management tools: Jenkins, ANT, Maven, Hudson, Docker, Kubernetes, Puppet, Chef, etc.\n\n\nKey Words: Sr. Full Stack Software Developer C# .NET React.js Angular HTML AWS S3 client server-side development Aerospace Avionics Defense\n\n\nBob A. Russ, MBA\nRegional Director of Recruiting\nM: 818.568.6948\nE: Bob.Russ@Entegee.com\nAKKODIS/Entegee\nentegee.com\n\n\nThis message is intended only for the designated recipient(s). It may contain confidential or proprietary information and may be subject to other contractual or confidentiality protection. If you are not a designated recipient, you may not review, copy or distribute this message. If you receive this message in error, please notify the sender by reply email and delete this message.\nEntegee, part of The Adecco Group North America headquartered at 10151 Deerwood Park Boulevard, Building 200, Suite 400, Jacksonville, Florida, 32256, is committed to data privacy compliance. To learn more about how we collect and process personal information, please read our General Privacy Policy located on the website above.".split("\n").flatMap(l => List(TextNode(l), br())),
              ),
              p(
                cls := "text-base text-gray-500",
                """If you have not yet done so, visit the Verifications section of
                            your Dashboard to upload your photo ID in your dashboard."""
              )
            )
          ),
          div(
            cls := "grid grid-cols-1 gap-4 pt-4 lg:grid-cols-3 lg:pt-12",
            div(
              cls := "flex flex-col flex-shrink-0 mb-6 lg:pr-12 md:mb-0",
              span(
                cls := "text-lg font-medium leading-6 text-black",
                "Java Developer"
              ),
              span(
                cls := "text-base text-gray-500",
                "Synechron"
              )
            ),
            div(
              cls := "lg:col-span-2",
              p(
                cls := "text-base text-gray-500",
                "• 6 to 9 years of industry experience of Agile development and scrums\n• Experience working with high volume, low latency, high throughput applications\n• Strong knowledge on Core Java, Spring(Core, MVC, JDBC), Hibernate/JDBC/JPA/ORM, JSON\n• Knowledge on front end technology (Angular) would be a plus.\n• Expertise in Web API implementations (Web services, Restful services etc.) .\n• Hands-on expertise in Springboot in a Java/J2EE environment would be a plus.\n• Strong knowledge on build (Ant/Maven), continuous integration (Jenkins), code quality analysis (SonarQube) and unit and integration testing (JUnit)\n• Exposure to SCM tool like bitbucket etc.\n• Should have strong Performance tuning and troubleshooting experience\n• Solid understanding of SOA concepts, RESTful API design\n• Responsible for designing, developing, testing, tuning and building a medium to large scale data processing system, particularly in Apache Spark\n• Ability to produce professional, technically-sound, and visually-appealing presentations and architecture designs\n• Experience creating high level technical/process documentation and presentations for audiences at various levels. Experience writing/editing technical, business, and process documentation in an Information Technology/Engineering environment\n• Must be able to understand requirements & convert to technical design and code\n• Knowledge of source code control systems, unit test framework, build and deployment tools\n• Must be able to work independently as well as in a team environment. Must be able to adapt to a rapidly changing environment\n• Hands on coding experience on Core Java and Spring\n• Strong analysis and design skills including OO design patterns".split("\n").flatMap(l => List(TextNode(l), br()))
              )
            )
          ),
          div(
            cls := "grid grid-cols-1 gap-4 pt-4 lg:grid-cols-3 lg:pt-12",
            div(
              cls := "flex flex-col flex-shrink-0 mb-6 lg:pr-12 md:mb-0",
              span(
                cls := "text-lg font-medium leading-6 text-black",
                "Python Developer"
              ),
              span(
                cls := "text-base text-gray-500",
                "Apex Systems"
              )
            ),
            div(
              cls := "lg:col-span-2",
              p(
                cls := "text-base text-gray-500",
                "Job#: 1333328\n\nJob Description:\n\nApex Systems/Bank of America has an opportunity open for a Python Developer. To apply please email your resume to cjsmith@ApexSystems.com\n\nThis is only a 1 round interview process, looking to move fast!\n\nJob Title: Python Developer\n\nClient: Bank of America\n\nLocation: Hybrid, 2 days onsite at any BOA location (Charlotte, Dallas, NJ, NYC etc.)\n\nCompensation: $60-70/hr on Apex W2\n\nDuration: 12 month contract project with the possibility of extension and FTE\n\nRequired Skills:\n• 5+ years of hands on Python experience \n• Experience with scripting languages like Shell and Bash \n• Agile Environment \n• Automation experience\n\n\nResponsibilities:\n• Good experience in command line interfaces (CLI), third party APIs and integration.\n• Good experience with webservers like Apache Tomcat\n• Good experience with frameworks like Django\n• Good knowledge of Linux, Windows, virtualization technologies\n• Experience building CI/CD pipeline, expert level knowledge of tools like git/Jenkins\n• Experience creating and maintaining complex data-driven automations and queries using SQL and noSQL databases.\n• Good proficiency in system, network, security and database operations, protocols and industry standard technologies.\n• Good experience in developing secure technologies, knowledge in ACLs and roles based entitlements.\n• Experience in systems analysis, modular design and creating API that support XML, JSON or other well-known interfaces.\n• Application development skills and experience in integrating automation within an existing back-end IT systems and\n\n\ndatabases.\n• Proven ability to work independently with minimal supervision and as part of a team with direct responsibilities.\n• Experience with IT core applications like DNS, Active Directory, Kerberos, SMTP, Transactional DBs, Apache, etc.\n• Ability to juggle competing priorities and adapt to changes in project scope.\n• Ability to communicate and collaborate effectively with teammates.\n• Effective verbal and written communication.\n• Good understanding of developing fault tolerant solutions and knowledge in horizontal scaling and resiliency/HA.\n\n\nFor this opportunity, you will also be eligible for benefits through Apex, a W2 hourly rate, weekly pay, and direct deposit!\n\nEEO Employer\n\nApex Systems is an equal opportunity employer. We do not discriminate or allow discrimination on the basis of race, color, religion, creed, sex (including pregnancy, childbirth, breastfeeding, or related medical conditions), age, sexual orientation, gender identity, national origin, ancestry, citizenship, genetic information, registered domestic partner status, marital status, disability, status as a crime victim, protected veteran status, political affiliation, union membership, or any other characteristic protected by law. Apex will consider qualified applicants with criminal histories in a manner consistent with the requirements of applicable law. If you have visited our website in search of information on employment opportunities or to apply for a position, and you require an accommodation in using our website for a search or application, please contact our Employee Services Department at employeeservices@apexsystems.com or 844-463-6178.\n\n4400 Cox Road\n\nSuite 200\n\nGlen Allen, Virginia 23060\n\nApex Systems is an equal opportunity employer. We do not discriminate or allow discrimination on the basis of race, color, religion, creed, sex (including pregnancy, childbirth, breastfeeding, or related medical conditions), age, sexual orientation, gender identity, national origin, ancestry, citizenship, genetic information, registered domestic partner status, marital status, disability, status as a crime victim, protected veteran status, political affiliation, union membership, or any other characteristic protected by law. Apex will consider qualified applicants with criminal histories in a manner consistent with the requirements of applicable law. If you have visited our website in search of information on employment opportunities or to apply for a position, and you require an accommodation in using our website for a search or application, please contact our Employee Services Department at employeeservices@apexsystems.com (Do not submit resumes or solicit consultants to this email address). UnitedHealthcare creates and publishes the Transparency in Coverage Machine-Readable Files on behalf of Apex Systems.".split("\n").flatMap(l => List(TextNode(l), br()))
              )
            )
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
