package io.micronaut.main.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.MultiLanguageGuide
import io.micronaut.SingleLanguageGuide
import io.micronaut.main.SiteMap
import io.micronaut.main.model.BuildStatus
import io.micronaut.Guide
import io.micronaut.MenuItem
import io.micronaut.TextMenuItem
import io.micronaut.pages.Page

@CompileStatic
class BuildStatusPage extends Page {

    String bodyClass = ''
    String title = 'Build Status'
    String slug = 'buildstatus.html'

    List<Guide> guides

    BuildStatusPage(List<Guide> guides) {
        this.guides = guides
    }

    @Override
    MenuItem menuItem() {
        new TextMenuItem(href: "${micronautUrl()}/buildstatus.html", title: 'Build Status')
    }

    @CompileDynamic
    String renderBuildStatusListAsTable(List<BuildStatus> buildStatusList,
                                        boolean useVersionColumn = false) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.table(style: 'width: 100%;') {
            thead {
                tr {
                    if (useVersionColumn) {
                        th 'Version'
                    }
                    th 'Build name'
                    th 'Status'
                }
            }
            tbody {
                for ( BuildStatus buildStatus : buildStatusList ) {
                    mkp.yieldUnescaped buildStatus.renderAsHtml(useVersionColumn)
                }
            }
        }
        writer.toString()
    }

    @CompileDynamic
    @Override
    String mainContent() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        List<BuildStatus> buildStatusList = SiteMap.BUILDS
        html.div(class: 'content container') {
            h1 {
                span 'Build'
                b 'Status'
            }
                div(class: 'twocolumns') {
                    div(class: 'column') {
                        section {
                            mkp.yieldUnescaped renderBuildStatusListAsTable(buildStatusList)
                            p {
                                mkp.yield 'Our'
                                a href: 'http://travis-ci.org/micronaut-projects/micronaut-core', 'continuous integration server'
                                mkp.yield ' runs on '
                                a href: 'http://www.travis-ci.org/', 'Travis CI'
                                mkp.yield ', and builds Micronaut and Micronaut Guides.'
                            }
                        }
                    }
                    div(class: 'column') {
                        List<BuildStatus> guideBuildStatuses = []
                        guides.each { Guide guide ->
                            if ( guide instanceof SingleLanguageGuide ) {
                                SingleLanguageGuide singleGuide = (SingleLanguageGuide) guide
                                guideBuildStatuses << new BuildStatus([
                                        title: guide.title,
                                        href : "https://travis-ci.org/${singleGuide.githubSlug}?branch=master",
                                        badge: "https://travis-ci.org/${singleGuide.githubSlug}.svg?branch=master",
                                        version: guide.versionNumber,
                                ])
                            } else if ( guide instanceof MultiLanguageGuide) {
                                MultiLanguageGuide multiLanguageGuide = (MultiLanguageGuide) guide
                                multiLanguageGuide.githubSlugs.each { k, v ->
                                    guideBuildStatuses << new BuildStatus([
                                            title: "${guide.title} ${k}",
                                            href : "https://travis-ci.org/${v}?branch=master",
                                            badge: "https://travis-ci.org/${v}.svg?branch=master",
                                            version: guide.versionNumber,])
                                }

                            }

                        }
                        mkp.yieldUnescaped renderBuildStatusListAsTable(guideBuildStatuses, true)
                    }
                }
        }
        writer.toString()
    }
}
