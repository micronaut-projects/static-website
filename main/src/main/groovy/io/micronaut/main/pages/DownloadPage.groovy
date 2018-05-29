package io.micronaut.main.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.main.SiteMap
import io.micronaut.GuideGroup
import io.micronaut.GuideGroupItem
import io.micronaut.MenuItem
import io.micronaut.TextMenuItem
import io.micronaut.pages.Page

@CompileStatic
class DownloadPage extends Page {
    String slug = 'download.html'
    String bodyClass = 'download'
    String title = 'Download'

    @Override
    MenuItem menuItem() {
        new TextMenuItem(href: "${micronautUrl()}/download.html", title: 'Download')
    }

    GuideGroup downloadGuideGroup() {
        String latestVersion = SiteMap.LATEST_VERSION
        new GuideGroup(title: "Download ${micronaut()} ${latestVersion}",
                image: "${getImageAssetPreffix()}download.svg",
                items: [
                        new GuideGroupItem(href: "https://github.com/micronaut-projects/micronaut-core/releases/tag/v${latestVersion}", title: 'Release Notes'),
                        new GuideGroupItem(href: "https://github.com/micronaut-projects/micronaut-core/releases/download/v${latestVersion}/micronaut-${latestVersion}.zip", title: 'Binary'),
                        new GuideGroupItem(href: "https://github.com/micronaut-projects/micronaut-core/releases/download/v${latestVersion}/micronaut-docs-${latestVersion}.zip", title: 'Documentation'),
                ])
    }

    @CompileDynamic
    String olderVersions() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: "transparent_post", style: 'margin-top: 0;') {
            h3 class: "columnheader", 'Older Versions'
            p "You can download previous versions as far back as Micronaut ${SiteMap.VERSIONS.last()}."
            div(class: "versionselector") {
                select(class: "form-control", onchange: "window.location.href='https://github.com/micronaut-projects/micronaut-core/releases/download/v'+ this.value +'/micronaut-' + this.value + '.zip'") {
                    option label: "Select a version", disabled: "disabled", selected: "selected"
                    for (String version : SiteMap.olderVersions()) {
                        option version
                    }
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
                span 'Download'
                b 'Micronaut'
            }
            div(class: 'twocolumns') {
                div(class: "odd column") {
                    mkp.yieldUnescaped downloadGuideGroup().renderAsHtmlWithStyleAttr('margin-bottom: 0;margin-top: 0;')
                    p(style: 'margin-top: 10px; margin-bottom: 0;') {
                        mkp.yield 'For historical release notes, refer to '
                        a href: "https://github.com/micronaut-projects/micronaut-core/releases", 'Github'
                    }
//                    mkp.yieldUnescaped olderVersions()
                }
                div(class: "column") {
                    div(class: 'transparent_post') {
                        p {
                            mkp.yield 'For a quick and effortless start on Mac OSX, Linux, or Cygwin, you can use '
                            a href: "http://sdkman.io", 'SDKMAN! (The Software Development Kit Manager)'
                            mkp.yield ' to download and configure any Micronaut version of your choice. '
                            mkp.yieldUnescaped '<br/>'
                            mkp.yield 'Windows users can use '
                            a href: "https://github.com/flofreud/posh-gvm", 'Posh-GVM'
                            mkp.yield ' (POwerSHell Groovy enVironment Manager), a PowerShell clone of the GVM CLI.'
                        }
                    }
                    article(class: "question", style: 'margin-top: 0;margin-bottom: 50px;') {
                        h3(class: 'columnheader', 'SDKMAN! (The Software Development Kit Manager)')
                        p('This tool makes installing Micronaut on any Bash platform (Mac OSX, Linux, Cygwin, Solaris, or FreeBSD) easy.')
                        p( 'Simply open a new terminal and enter:')
                        div(class: 'code') {
                            p '$ curl -s get.sdkman.io | bash'
                        }
                        p('Follow the on-screen instructions to complete installation.')
                        p( 'Open a new terminal or type the command:')
                        div(class: 'code') {
                            p '$ source "$HOME/.sdkman/bin/sdkman-init.sh"'
                        }
                        p 'Then install the latest stable Micronaut:'
                        div(class: 'code') {
                            p '$ sdk install micronaut'
                        }
                        p 'After installation is complete and you\'ve made it your default version, test it with:'
                        div(class: 'code') {
                            p '$ mn -version'
                        }
                        p 'That\'s all there is to it!'
                    }
                }
            }
        }
        writer.toString()
    }
}
