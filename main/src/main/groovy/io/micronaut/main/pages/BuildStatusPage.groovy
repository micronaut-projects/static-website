package io.micronaut.main.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.GuideGroupItem
import io.micronaut.MultiLanguageGuide
import io.micronaut.SingleLanguageGuide
import io.micronaut.main.SiteMap
import io.micronaut.main.model.BuildStatus
import io.micronaut.Guide
import io.micronaut.MenuItem
import io.micronaut.TextMenuItem
import io.micronaut.pages.Page
import org.yaml.snakeyaml.Yaml

@CompileStatic
class BuildStatusPage extends Page {

    String bodyClass = ''
    String title = 'Build Status'
    String slug = 'buildstatus.html'

    private static final String GITHUB_ORG = 'micronaut-projects'
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



        html.div(class: 'content container') {
            h1 {
                span 'Build'
                b 'Status'
            }
            div(class: 'twocolumns') {
                div(class: 'column') {
                    section {
                        mkp.yieldUnescaped renderBuildStatusListAsTable(coreBuilds(), false)
                        p {
                            mkp.yield 'Our'
                            a href: 'http://travis-ci.org/micronaut-projects/micronaut-core', 'continuous integration server'
                            mkp.yield ' runs on '
                            a href: 'http://www.travis-ci.org/', 'Travis CI'
                            mkp.yield ', and builds Micronaut and Micronaut Guides.'
                        }
                    }
                    mkp.yieldUnescaped renderBuildStatusListAsTable(modulesBuildStatus(), false)
                }
                div(class: 'column') {
                    mkp.yieldUnescaped renderBuildStatusListAsTable(guideBuildStatuses(), true)
                }
            }
        }
        writer.toString()
    }

    @CompileDynamic
    List<BuildStatus> guideBuildStatuses() {
        List<BuildStatus> guideBuildStatuses = []
        guides.each { Guide guide ->
            if ( guide instanceof SingleLanguageGuide ) {
                SingleLanguageGuide singleGuide = (SingleLanguageGuide) guide
                guideBuildStatuses << buildStatus(guide.title, singleGuide.githubSlug, 'master', guide.versionNumber)
            } else if ( guide instanceof MultiLanguageGuide) {
                MultiLanguageGuide multiLanguageGuide = (MultiLanguageGuide) guide
                multiLanguageGuide.githubSlugs.each { k, v ->
                    guideBuildStatuses << buildStatus("${guide.title} ${k}", v, 'master', guide.versionNumber)
                }
            }
        }
        guideBuildStatuses
    }

    @CompileDynamic
    List<BuildStatus> modulesBuildStatus() {
        final String githubOrg = 'micronaut-projects'
        final String branch = 'master'
        Yaml yaml = new Yaml()
        File f = new File('src/main/resources/modules.yml')
        Map model = yaml.load(f.newDataInputStream())
        model['modules'].collect { k, v ->
            buildStatus(v.label, "${githubOrg}/${v.githubslug}".toString(), branch, null)
        }
    }

    BuildStatus buildStatus(String title, String githubSlug, String branch, String versionNumber) {
        new BuildStatus([
                title: title,
                href : "https://travis-ci.org/${githubSlug}?branch=${branch}",
                badge: "https://travis-ci.org/${githubSlug}.svg?branch=${branch}",
                version: versionNumber,
        ])
    }

    List<BuildStatus> coreBuilds() {
        ['master', '1.1.x', '1.0.x'].collect { String branch ->
            String title = 'Micronaut Core'
            String githubRepo = 'micronaut-core'
            String githubSlug = "${GITHUB_ORG}/${githubRepo}"
            buildStatus("$title $branch", githubSlug, branch, null)
        } as List<BuildStatus>
    }
}
