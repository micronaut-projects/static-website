package io.micronaut.main.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.Navigation
import io.micronaut.main.SiteMap
import io.micronaut.MenuItem
import io.micronaut.TextMenuItem
import io.micronaut.main.model.Question
import io.micronaut.pages.Page

@CompileStatic
class AnnouncementPage extends Page {
    String title = 'Announcement'
    String slug  = 'announcement.html'
    String bodyClass = 'announcement'

    @Override
    MenuItem menuItem() {
        Navigation.desktopBlogMenuItem(micronautUrl())
    }

    @CompileDynamic
    @Override
    String mainContent() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'content') {
            article(class: 'container') {
                h1 {
                    span 'Micronaut'
                    b 'Announcements'
                }
                announcements().each { Map announcement ->
                    div(class: 'announcement') {
                        h2 {
                            span class: 'align-right', announcement.published
                            b class: 'align-left', announcement.title
                        }
                        div {
                            announcement.paragraphs.each { paragraph ->
                                p {
                                    mkp.yieldUnescaped paragraph
                                }
                            }
                        }
                    }
                }
            }
        }
        writer.toString()
    }

    List<Map<String, Object>> announcements() {
        [
                [
                        title: 'Micronaut Open Sourced',
                        published: 'May 23rd, 2018',
                        paragraphs: ['Today, it is with great pleasure that we <a href="https://github.com/micronaut-projects/micronaut-core">open source Micronaut on Github</a>.',
         'Micronaut represents a significant advancement in how a range of application types – with a particular focus on microservices – are built for the JVM.',
         'Micronaut comes to you from <a href="https://objectcomputing.com">OCI</a> and the team that built the <a href="http://grails.org">Grails framework</a>. <b>It builds upon over 10 years of experience creating application frameworks for the JVM</b>.',
         'By leveraging the lessons learned building the Grails framework, we have built a toolkit that encompasses all the modern features developers have come to expect from a framework, including dependency injection, AOP, configuration management, and more.',
            'With Micronaut, however, we’ve greatly reduced the runtime overhead – in terms of memory consumption and startup time – found in traditional frameworks. This is achieved through <b>the use of annotation processors that pre-compile all the necessary metadata and information needed to run your application ahead of time, eliminating the need for reflection and cached reflective metadata at the framework level</b>.',

           'Micronaut also features both an HTTP client and an HTTP server built on Netty, plus a range of tools to aid deployment into a cloud environment.',

           'Micronaut supports building applications in <b>Groovy, Java, and Kotlin</b>. Its design takes heavy inspiration from Spring and Grails to ensure that it is as simple as possible for developers to become fully proficient with the framework.',

           "For more about these features, view the comprehensive <a href='${Navigation.documentationMenuItem(micronautUrl()).href}'>Snapshot documentation</a>.",

           'Next week at <a href="https://gr8conf.eu">Gr8Conf Europe</a> in Denmark, our team will deliver numerous talks and workshops on Micronaut for those interested in getting a head start using the Framework. (Find out where else you can see <a href="https://objectcomputing.com/news/2018/05/10/launching-micro-future-worldwide">Micronaut in action</a>.)',

           'We will also be releasing the <b>first milestone of Micronaut 1.0 next week</b>, which will be followed quickly with regular milestone releases and ultimately a <b>GA release later this year</b>.',

           'Over the next few months, we will be working diligently to build out the Framework’s features and capabilities, so please stay tuned and feel free to <a href="https://github.com/micronaut-projects/micronaut-core/issues">submit your ideas and suggestions to us</a>.',

           'With warm regards, <b>Graeme Rocher</b>']
                ],
                [
                        title: 'Micronaut Announcement at Greach Conference',
                        published: 'Apr 28th, 2018',
                        paragraphs: [
                                '<div class="desktop", style="width: 560px;margin: 0 auto";><iframe width="560" height="315" src="https://www.youtube.com/embed/56j_f3OCg6E" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>',
                                '<div class="mobile", style="width: 380px;margin: 0 auto";><iframe width="380" height="218" src="https://www.youtube.com/embed/56j_f3OCg6E" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>',
                        ]
                ]
        ] as List<Map<String, Object>>
    }
}
