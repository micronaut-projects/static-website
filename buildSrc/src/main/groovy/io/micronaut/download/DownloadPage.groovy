package io.micronaut.download

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.documentation.GuideGroup
import io.micronaut.documentation.GuideGroupItem
import io.micronaut.documentation.SiteMap
import io.micronaut.documentation.SoftwareVersion

@CompileStatic
class DownloadPage {
    static String getImageAssetPreffix(String url) {
        url + '/images/'
    }

    static GuideGroup downloadGuideGroup(File releases, String url) {
        SoftwareVersion latest = SiteMap.latestVersion(releases)
        String latestVersion = latest.versionText
        new GuideGroup(title: "Download Micronaut ${latestVersion}",
                image: "${getImageAssetPreffix(url)}download.svg",
                items: [
                        new GuideGroupItem(href: "https://github.com/micronaut-projects/micronaut-core/releases/tag/v${latestVersion}", title: 'Release Notes'),
                        new GuideGroupItem(href: "https://github.com/micronaut-projects/micronaut-starter/releases/download/v${latestVersion}/micronaut-cli-${latestVersion}.zip", title: 'Binary'),
                ])
    }

    static GuideGroup installationGuideGroup(String url) {
        new GuideGroup(title: "Install",
                image: "${getImageAssetPreffix(url)}download.svg",
                items: [
                        new GuideGroupItem(href: "https://docs.micronaut.io/latest/guide/index.html#installSdkman", title: 'Install with SDKman'),
                        new GuideGroupItem(href: "https://docs.micronaut.io/latest/guide/index.html#installHomebrew", title: 'Install with Homebrew'),
                        new GuideGroupItem(href: "https://docs.micronaut.io/latest/guide/index.html#installMacPorts", title: 'Install with MacPorts'),
                        new GuideGroupItem(href: "https://docs.micronaut.io/latest/guide/index.html#installWindows", title: 'Install through Binary on Windows'),
                        new GuideGroupItem(href: "https://docs.micronaut.io/latest/guide/index.html#buildSource", title: 'Building from Source'),
                ])
    }

    @CompileDynamic
    static String mainContent(File releases, String url) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'content container') {
            h1 {
                span 'Download'
                b {
                    mkp.yieldUnescaped 'Micronaut<sup>&reg;</sup>'
                }
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
                        pre(class: 'language-shell') {
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
                    mkp.yieldUnescaped downloadGuideGroup(releases, url).renderAsHtmlWithStyleAttr('margin-bottom: 0;margin-top: 0;')
                    p(style: 'margin-top: 10px; margin-bottom: 0;') {
                        mkp.yield 'For historical release notes, refer to '
                        a href: "https://github.com/micronaut-projects/micronaut-core/releases", 'Github'
                        mkp.yieldUnescaped installationGuideGroup(url).renderAsHtmlWithStyleAttr('margin-bottom: 0;margin-top: 30px;')
                    }
                }
            }
        }
        writer.toString()
    }
}
