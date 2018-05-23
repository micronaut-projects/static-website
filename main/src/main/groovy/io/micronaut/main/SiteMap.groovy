package io.micronaut.main

import groovy.transform.CompileStatic
import io.micronaut.main.model.BuildStatus
import io.micronaut.main.model.DocumentationGroup
import io.micronaut.main.model.DocumentationLink
import io.micronaut.main.model.Event
import io.micronaut.main.model.Question
import io.micronaut.main.pages.*
import io.micronaut.SoftwareVersion
import io.micronaut.pages.HtmlPage
import io.micronaut.pages.Page

@CompileStatic
class SiteMap {

    public final static List<HtmlPage> PAGES = [
            new HomePage(),
            new DownloadPage(),
            new QuestionPage(),
            new AnnouncementPage(),
            new SupportPage(),
            new DocumentationPage(),
            new EventsPage(),
    ] as List<HtmlPage>

    public final static List<BuildStatus> BUILDS = [
            new BuildStatus([title: "${Page.micronaut()} Master",
                             href: "https://travis-ci.org/micronaut-projects/micronaut-core?branch=master", badge: "https://travis-ci.org/micronaut-projects/micronaut-core.svg?branch=master"]),
    ]

    public final static List<Event> EVENTS = [
//            new Event(image: "confs/greach.png",
//                    href: "http://greachconf.com",
//                    title: "Greach: The Groovy Spanish Conference",
//                    location: "Madrid, Spain",
//                    dates: "March 15 - 17, 2018",
//                    about: "Enhance your knowledge and skills with some of the most recognizable names from the Groovy development scene from all around the world. Experience three days full of Micronaut, Groovy and Grails talks and networking ... plus our famous Friday Beers Party!"),
            new Event(image: "confs/gr8confeu.png",
                    href: "http://gr8conf.eu/",
                    title: "GR8Conf EU 2018",
                    location: "Copenhagen, Denmark",
                    dates: "May 30 - June 1, 2018",
                    about: """${Page.micronaut()}, Devops, Groovy, Grails, and related technologies have seen astounding growth in interest and adoption over the past few years, and with good reason. GR8Conf is a series of conferences founded to spread the word worldwide. The 2018 GR8Conf Europe is celebrating its 10th year, and it's expected to be a blast. As in 2017 the conference had a DevOps day, this year DevOps topics will be mixed with the rest of the topics. GR8Conf is an independent, affordable series of conferences and covers All Things Groovy"""),
            new Event(image: "confs/gr8confus.png",
                    href: "http://gr8conf.us/",
                    title: "GR8Conf US 2018",
                    location: "Minneapolis, MN, USA",
                    dates: "July 25 - July 27, 2018",
                    about: """${Page.micronaut()}, Devops, Groovy, Grails, and related technologies have seen astounding growth in interest and adoption over the past few years, and with good reason. GR8Conf is a series of conferences founded to spread the word worldwide. GR8Conf is an independent, affordable series of conferences and covers All Things Groovy"""),

    ]

    public final static List<String> VERSIONS = [
            "1.0.0"
    ]

    public final static String LATEST_VERSION = VERSIONS[0]
    public final static List<String> OLDER_VERSIONS = VERSIONS.drop(1)
    static List<String> olderVersions() {
        List<SoftwareVersion> versionList = OLDER_VERSIONS.collect { String version ->
            SoftwareVersion.build(version)
        }
        Collections.sort(versionList)
        versionList = versionList.reverse()
        versionList.collect { SoftwareVersion version -> version.versionText }
    }

    public final static List<Question> QUESTIONS = [
            new Question(slug: 'question_opensource',title: "How is ${Page.micronaut()} licensed?", answer: """
${Page.micronaut()} is an Open Source project licensed under the [Apache License v2](http://www.apache.org/licenses/LICENSE-2.0)."""),

            new Question(slug: 'question_learn',title: "What is the best way to get started learning ${Page.micronaut()}?", answer: """
The definitive guide to developing with ${Page.micronaut()} is the [User Guide](https://micronaut-projects.github.io/micronaut-core/snapshot/guide/index.html).
"""), //TODO  In addition, step-by-step tutorials for solving common scenarios can be found at [Micronaut Guides](http://guides.micronaut.io).
            new Question(slug: 'question_training',title: "Are there training offerings for ${Page.micronaut()}?", answer: """
[Object Computing, Inc](http://objectcomputing.com) offers [training courses](https://objectcomputing.com/training/schedule?track=34&instructor=&time=#oci-schedule-filters-form) which are developed and delivered by the ${Page.micronaut()} founders and core development team.</p>
"""),
            new Question(slug: 'question_grails',title: "How does ${Page.micronaut()} relate to Grails/Spring Boot?", answer: """
The inventors of the [Grails framework](http://grails.org) are developing the ${Page.micronaut()} project, taking many of the lessons learned from Grails into account when designing the new framework. Unlike Grails and other JVM web frameworks, ${Page.micronaut()} is designed to function as both a client and a server framework in a microservice environment. ${Page.micronaut()}'s modularity and lack of external dependencies means that Grails developers can take advantage of many key ${Page.micronaut()} features within their existing Grails applications.
"""),
            //TODO  Grails integration is a priority of the ${Page.micronaut()} developers, so please stay tuned to the [Micronaut Guides](http://guides.micronaut.io) for future information.
//TODO:- check out [the Grails/Micronaut guide](http://grails.org) to learn more.

            new Question(slug: 'question_usage',title: "Where can I interact with  ${Page.micronaut()} developers and community?", answer: """
The best place to chat with developers and users of ${Page.micronaut()} is the [Gitter community](https://gitter.im/micronautfw/). Drop in and join our community!
"""),

            new Question(slug: 'question_usage',title: "Where can I ask questions about a specific programming problem related to ${Page.micronaut()}?", answer: """
The ${Page.micronaut()} development team makes a priority of monitoring [Stack Overflow](http://stackoverflow.com/questions/tagged/micronaut) for technical questions with ${Page.micronaut()}. If you have a specific technical question about usage of the framework, we recommend you post your question to Stack Overflow using the [#micronaut tag](http://stackoverflow.com/questions/tagged/micronaut).
"""),

            new Question(slug: 'question_mailinglist',
                    title: "Do you have a mailing list?",
                    answer: """
We don't have a mailing list. As mentioned above, for technical questions about ${Page.micronaut()}, please post using our official [#micronaut tag](http://stackoverflow.com/questions/tagged/micronaut) on [Stack Overflow](http://stackoverflow.com/questions/tagged/micronaut). For general community interaction, please join our [Gitter Community](https://gitter.im/micronautfw).
"""),
            new Question(slug: 'question_socialmedia',title: "Are you on social media?", answer: """
Yes! You can get news about ${Page.micronaut()} on [Twitter](http://twitter.com/micronautfw).
"""), //TODO  and [LinkedIn](https://www.linkedin.com/showcase/micronaut/).
            new Question(slug: 'question_feature',
                   title: "I have a cool idea or feature request for ${Page.micronaut()} - where can I share it?",
                    answer: """
Glad you asked! Our [Gitter community](https://gitter.im/micronautfw) is a great place to start a conversation with the ${Page.micronaut()} developers and other ${Page.micronaut()} users. If you would like ot make a formal feature request, please [create an issue](https://github.com/micronaut-projects/micronaut-core/issues/new).
"""),
            new Question(slug: 'question_issue',title: "I think I\'ve found a bug in ${Page.micronaut()}, where can I report it?", answer: """
The ${Page.micronaut()} project uses [Github issues](https://github.com/micronaut-projects/micronaut-core/issues) to report and track issues, feature enhancements, and new features. If you're logged in to your Github account, use this link to [report an issue](https://github.com/micronaut-projects/micronaut-core/issues/new).
"""),
            new Question(slug: 'question_docs',title: "Can I contribute to the documentation?", answer: """
Absolutely! The ${Page.micronaut()} project includes a few forms of documentation:

- The [reference documentation](https://micronaut-projects.github.io/micronaut-core/snapshot/guide/index.html) contains language specification, user guides, a getting started tutorial, and more.</li>
- The [JavaDoc APIs](https://micronaut-projects.github.io/micronaut-core/snapshot/api/) documents the classes of the ${Page.micronaut()} code base</li>
- [GitHub](https://github.com/micronaut-projects/static-website) allows users to contribute to this website.</li>

Contributing to the ${Page.micronaut()} documentation is fairly easy. Create a GitHub account or sign in with an existing account, browse to the [Latest Guide](https://micronaut-projects.github.io/micronaut-core/snapshot/guide/index.html) and click the "Improve this doc" button at the top of the section you wish to edit. Please don't hesitate to help us make improvements, fix typos or broken language, clarify complicated sections, add new material, and anything else you feel will be helpful to other ${Page.micronaut()} users. And thank you! 
"""),
            new Question(slug: 'question_code',title: "Can I contribute code to the ${Page.micronaut()} framework?", answer: """
Yes please! If you are looking to make an initial contribution, just raise your hand on the [Gitter Community](https://gitter.im/micronautfw) and let us know if you have a particular area of interest you'd like to work on, or if you're just looking for a good issue to get started on. The ${Page.micronaut()} development team can provide guidance on how best to tackle a particular problem, collaborate on implementation ideas, and discuss the semantics or scope of the proposed change. Please see the [contributing instructions](https://github.com/micronaut-projects/micronaut-core/blob/master/CONTRIBUTING.md) for more detail.

"""),
            new Question(slug: 'question_trademark', title: "I see the ${Page.micronaut()} name is trademarked, does that mean it’s proprietary?", answer: """
Not at all, the ${Page.micronaut()} name is a trademark of OCI but this does not affect your rights to use and modify the source code under the terms of the [Apache License v2](http://www.apache.org/licenses/LICENSE-2.0).

"""),
            //TODO: Update infrastructure notes - using JFrog not Artifactory - discuss with James
            new Question(slug: 'question_sponsors', title: "Who sponsors development of ${Page.micronaut()}?", answer: """
OCI sponsors ${Page.micronaut()}'s development, maintains the ${Page.micronaut()} framework and employs key members of the ${Page.micronaut()} team.

In addition, [JFrog](http://www.jfrog.com/) provides the infrastructure for deploying and hosting our snapshots and releases, thanks to the [Bintray](https://bintray.com/) social platform for distribution and the [JFrog OSS](https://oss.jfrog.org/) repository.

"""),
    ]

}
