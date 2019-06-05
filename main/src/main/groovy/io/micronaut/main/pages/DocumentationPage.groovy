package io.micronaut.main.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.GuideGroup
import io.micronaut.GuideGroupItem
import io.micronaut.MenuItem
import io.micronaut.Navigation
import io.micronaut.SoftwareVersion
import io.micronaut.main.SiteMap
import io.micronaut.pages.Page
import org.yaml.snakeyaml.Yaml

@CompileStatic
class DocumentationPage extends Page {
    String title = 'Documentation'
    String slug = 'documentation.html'
    String bodyClass = 'docs'

    @CompileDynamic
    List<GuideGroupItem> extraLinksItems() {
        Yaml yaml = new Yaml()
        File f = new File('src/main/resources/modules.yml')
        Map model = yaml.load(f.newDataInputStream())
        model['modules'].collect { k, v ->
            final String url = "https://micronaut-projects.github.io/${v.githubslug}/${v.version}/guide/index.html"
            final String title = "${v.label}${v.version.equalsIgnoreCase('snapshot') ? ' (SNAPSHOT)' : ''}"
            new GuideGroupItem(href: url, title: title, legend: v.legend ?: '', image: v.image)
        }.sort { a, b ->
            a.title.replace("Micronaut", "").replaceAll('^\\s+|\\s+$', "") <=> b.title.replace("Micronaut", "").replaceAll('^\\s+|\\s+$', "")
        }
    } 

    @Override
    MenuItem menuItem() {
        Navigation.documentationMenuItem(micronautUrl())
    }

    GuideGroup preReleaseDocumentationGuideGroup() {
        SoftwareVersion version = SiteMap.latestPreReleaseVersion()
        if (!version) {
            return null
        }
        new GuideGroup(title: "Pre-Release Version (${version.versionText}) Documentation",
                image: "${getImageAssetPreffix()}documentation.svg",
                items: [
                        new GuideGroupItem(href: "http://docs.micronaut.io/${version.versionText}/guide/index.html", title: 'User Guide (Single page)'),
                        new GuideGroupItem(href: "http://docs.micronaut.io/${version.versionText}/guide/introduction.html", title: 'User Guide (Multi-page)'),
                        new GuideGroupItem(href: "http://docs.micronaut.io/${version.versionText}/api/", title: 'API Reference'),
                ])
    }

    GuideGroup latestDocumentationGuideGroup() {
        SoftwareVersion version = SiteMap.latestVersion()
        new GuideGroup(title: "Latest Version (${version.versionText}) Documentation",
                image: "${getImageAssetPreffix()}documentation.svg",
                items: [
                        new GuideGroupItem(href: "http://docs.micronaut.io/latest/guide/index.html", title: 'User Guide'),
                        new GuideGroupItem(href: "http://docs.micronaut.io/latest/api/", title: 'API Reference'),
                ])
    }

    GuideGroup documentationGuideGroup(String title, List<GuideGroupItem> items, String imageName = 'documentation.svg') {
        new GuideGroup(title: title,
                image: "${getImageAssetPreffix()}${imageName}",
                items: items)
    }

    GuideGroup snapshotDocumentationGuideGroup() {
        new GuideGroup(title: 'Snapshot Documentation',
                image: "${getImageAssetPreffix()}documentation.svg",
                items: [

                        new GuideGroupItem(href: "http://docs.micronaut.io/snapshot/guide/index.html", title: 'User Guide'),
                        new GuideGroupItem(href: "http://docs.micronaut.io/snapshot/api/", title: 'API Reference'),

                ])
    }

    @CompileDynamic
    @Override
    String mainContent() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)

        GuideGroup preRelease = preReleaseDocumentationGuideGroup()
        html.div(class:"content container") {
            h1 {
                span 'Micronaut'
                b 'Documentation'
            }
            div(class: "twocolumns") {
                div(class: "odd column") {
                    mkp.yieldUnescaped latestDocumentationGuideGroup().renderAsHtml()
                }

                div(class: "column") {
                    GuideGroup guideGroup = shouldDisplayPreRelease() ? preRelease : snapshotDocumentationGuideGroup()
                    mkp.yieldUnescaped guideGroup.renderAsHtml()
                }
            }
            div(class: "twocolumns") {
                List links = extraLinksItems()
                div(class: "odd column") {
                    List oddlinks = links.subList(0, (int) (links.size() / 2))
                    mkp.yieldUnescaped documentationGuideGroup('Other Modules', oddlinks).renderAsHtml()

                }
                div(class: "column") {
                    List evenlinks = links.subList((int) (links.size() / 2), links.size())
                    mkp.yieldUnescaped documentationGuideGroup('Other Modules', evenlinks).renderAsHtml()

                }
            }
            div(class: "twocolumns") {
                div(class: "odd column") {
                    html.div(class: "guidegroup") {
                        div(class: "guidegroupheader") {
                            String title = 'Documentation of older versions'
                            String image = "${getImageAssetPreffix()}documentation.svg"
                            img src: image, alt: title
                            h2 {
                                html.mkp.yieldUnescaped title
                            }
                        }
                        ul {
                            li {
                                span 'User Guide'
                                select(onchange: "window.location.href='https://docs.micronaut.io/' + this.value + '/guide/index.html'") {
                                    option 'Select a version'
                                    for (String version : SiteMap.olderVersions()) {
                                        option version
                                    }
                                }
                            }
                            li {
                                span 'API Reference'
                                select(onchange: "window.location.href='https://docs.micronaut.io/' + this.value + '/api'") {
                                    option 'Select a version'
                                    for (String version : SiteMap.olderVersions()) {
                                        option version
                                    }

                                }
                           }
                        }
                    }
                }

                if (shouldDisplayPreRelease()) {
                    div(class: "column") {
                        mkp.yieldUnescaped snapshotDocumentationGuideGroup().renderAsHtml()
                    }
                }
            }
        }
        writer.toString()
    }

    private boolean shouldDisplayPreRelease() {
        SoftwareVersion preRelease = SiteMap.latestPreReleaseVersion()
        SoftwareVersion latest = SiteMap.latestVersion()
        int compare = preRelease.compareTo(latest)
        return preRelease && compare > 0

    }
}
