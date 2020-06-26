package io.micronaut.main.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.SoftwareVersion
import io.micronaut.main.SiteMap
import io.micronaut.GuideGroup
import io.micronaut.GuideGroupItem
import io.micronaut.MenuItem
import io.micronaut.TextMenuItem
import io.micronaut.pages.Page

@CompileStatic
class DownloadPage extends PageWithSyntaxHighlight {
    String slug = 'download.html'
    String bodyClass = 'download'
    String title = 'Download'

    @Override
    MenuItem menuItem() {
        new TextMenuItem(href: "${micronautUrl()}/download.html", title: 'Download')
    }

    GuideGroup downloadGuideGroup() {
        SoftwareVersion latest = SiteMap.latestVersion()
        String latestVersion = latest.versionText
        new GuideGroup(title: "Download ${micronaut()} ${latestVersion}",
                image: "${getImageAssetPreffix()}download.svg",
                items: [
                        new GuideGroupItem(href: "https://github.com/micronaut-projects/micronaut-core/releases/tag/v${latestVersion}", title: 'Release Notes'),
                        new GuideGroupItem(href: "https://github.com/micronaut-projects/micronaut-starter/releases/download/v${latestVersion}/micronaut-cli-${latestVersion}.zip", title: 'Binary'),
                ])
    }

    GuideGroup installationGuideGroup() {
        new GuideGroup(title: "Install",
                image: "${getImageAssetPreffix()}download.svg",
                items: [
                        new GuideGroupItem(href: "https://docs.micronaut.io/latest/guide/index.html#installSdkman", title: 'Install with SDKman'),
                        new GuideGroupItem(href: "https://docs.micronaut.io/latest/guide/index.html#installHomebrew", title: 'Install with Homebrew'),
                        new GuideGroupItem(href: "https://docs.micronaut.io/latest/guide/index.html#installMacPorts", title: 'Install with MacPorts'),
                        new GuideGroupItem(href: "https://docs.micronaut.io/latest/guide/index.html#installWindows", title: 'Install through Binary on Windows'),
                        new GuideGroupItem(href: "https://docs.micronaut.io/latest/guide/index.html#buildSource", title: 'Building from Source'),
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
                    div(class: 'transparent_post desktop') {
                        p {
                            mkp.yield 'For a quick and effortless start on Mac OSX, Linux, or Cygwin, you can use '
                            a href: "http://sdkman.io", 'SDKMAN! (The Software Development Kit Manager)'
                            mkp.yield ' to download and configure any Micronaut version of your choice. '
                        }
                    }
                    article(class: "question desktop", style: 'margin-top: 0;margin-bottom: 50px;') {
                        h3(class: 'columnheader', 'Installing with SDKMAN!')
                        p('This tool makes installing Micronaut on any Unix based platform (Mac OSX, Linux, Cygwin, Solaris, or FreeBSD) easy.')
                        p( 'Simply open a new terminal and enter:')
                        pre(class
                        : 'language-shell') {
                            code('')
                            code('$ curl -s https://get.sdkman.io | bash')
                        }

                        p('Follow the on-screen instructions to complete installation.')
                        p( 'Open a new terminal or type the command:')
                        pre(class: 'language-shell') {
                            code('')
                            code('$ source "$HOME/.sdkman/bin/sdkman-init.sh"')
                        }
                        p 'Then install the latest stable Micronaut:'
                        pre(class: 'language-shell') {
                            code('')
                            code('$ sdk install micronaut')
                        }
                        p 'If prompted, make this your default version. After installation is complete it can be tested with:'
                        pre(class: 'language-shell') {
                            code('')
                            code('$ mn --version')
                        }
                        p 'That\'s all there is to it!'
                    }
                }
                div(class: "column") {
                    mkp.yieldUnescaped downloadGuideGroup().renderAsHtmlWithStyleAttr('margin-bottom: 0;margin-top: 0;')

                    p(style: 'margin-top: 10px; margin-bottom: 0;') {
                        mkp.yield 'For historical release notes, refer to '
                        a href: "https://github.com/micronaut-projects/micronaut-core/releases", 'Github'
                    mkp.yieldUnescaped installationGuideGroup().renderAsHtmlWithStyleAttr('margin-bottom: 0;margin-top: 30px;')

                    }

                }
            }
        }
        writer.toString()
    }

}
