package io.micronaut.documentation

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import org.yaml.snakeyaml.Yaml

@CompileStatic
class DocumentationPage {

    static String getImageAssetPreffix(String url) {
        url + '/images/'
    }

    @CompileDynamic
    static String mainContent(File releases, File modules, String url) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)

        GuideGroup preRelease = preReleaseDocumentationGuideGroup(releases, url)
        html.div(class:"content container") {
            h1 {
                span {
                    mkp.yieldUnescaped 'Micronaut<sup>&reg;</sup>'
                }
                b 'Documentation'
            }
            div(class: "twocolumns mobile") {
                div(class: "odd column") {
                    mkp.yieldUnescaped latestDocumentationGuideGroup(releases, url).renderAsHtml()
                }
                div(class: "column") {
                    GuideGroup guideGroup = shouldDisplayPreRelease(releases) ? preRelease : snapshotDocumentationGuideGroup(url)
                    mkp.yieldUnescaped guideGroup.renderAsHtml()
                }
            }
            div(class: "twocolumns desktop") {
                div(class: "odd column") {
                    GuideGroup guideGroup = shouldDisplayPreRelease(releases) ? preRelease : snapshotDocumentationGuideGroup(url)
                    mkp.yieldUnescaped guideGroup.renderAsHtml()
                }

                div(class: "column") {
                    mkp.yieldUnescaped latestDocumentationGuideGroup(releases, url).renderAsHtml()
                }
            }
            div(class: "twocolumns") {
                List<GuideGroupItem> links = extraLinksItems(modules, url)
                div(class: "odd column") {
                    mkp.yieldUnescaped documentationGuideGroup('Misc', links.findAll { it.category == 'Misc' }, url,  'micronautaprrentice.svg').renderAsHtml()
                    mkp.yieldUnescaped documentationGuideGroup('Messaging', links.findAll { it.category == 'Messaging' }, url,  "messaging.svg").renderAsHtml()
                    mkp.yieldUnescaped documentationGuideGroup('Cloud', links.findAll { it.category == 'Cloud' }, url,  "cloud.svg").renderAsHtml()
                    mkp.yieldUnescaped documentationGuideGroup('API', links.findAll { it.category == 'API' }, url,  'api.svg').renderAsHtml()

                    olderVersionBlock(html, releases, url)

                }
                div(class: "column") {
                    mkp.yieldUnescaped documentationGuideGroup('Data Access', links.findAll { it.category == 'Data Access' }, url, 'dataaccess.svg').renderAsHtml()

                    mkp.yieldUnescaped documentationGuideGroup('Database Migration', links.findAll { it.category == 'Database Migration' }, url, 'databasemigration.svg').renderAsHtml()
                    mkp.yieldUnescaped documentationGuideGroup('Analytics', links.findAll { it.category == 'Analytics' }, url, 'analytics.svg').renderAsHtml()

                    mkp.yieldUnescaped documentationGuideGroup('Views', links.findAll { it.category == 'Views' }, url,  'views.svg').renderAsHtml()
                    mkp.yieldUnescaped documentationGuideGroup('Languages', links.findAll { it.category == 'Languages' }, url, 'languages.svg').renderAsHtml()
                    mkp.yieldUnescaped documentationGuideGroup('Reactive', links.findAll { it.category == 'Reactive' }, url,  'reactive.svg').renderAsHtml()

                }
            }

        }
        writer.toString()
    }

    private static GuideGroup documentationGuideGroup(String title,
                                               List<GuideGroupItem> items,
                                                String url,
                                               String imageName = 'documentation.svg') {
        new GuideGroup(title: title,
                image: "${getImageAssetPreffix(url)}${imageName}",
                items: items)
    }

    private  static GuideGroup latestDocumentationGuideGroup(File releases, String url) {
        SoftwareVersion version = SiteMap.latestVersion(releases)
        new GuideGroup(title: "Latest Version (${version.versionText}) Documentation",
                image: "${getImageAssetPreffix(url)}documentation.svg",
                items: [
                        new GuideGroupItem(href: "http://docs.micronaut.io/latest/guide/index.html", title: 'User Guide'),
                        new GuideGroupItem(href: "http://docs.micronaut.io/latest/api/", title: 'API Reference'),
                ])
    }

    private static GuideGroup preReleaseDocumentationGuideGroup(File releases, String url) {
        SoftwareVersion version = SiteMap.latestPreReleaseVersion(releases)
        if (!version) {
            return null
        }
        new GuideGroup(title: "Pre-Release Version (${version.versionText}) Documentation",
                image: "${getImageAssetPreffix(url)}documentation.svg",
                items: [
                        new GuideGroupItem(href: "http://docs.micronaut.io/${version.versionText}/guide/index.html", title: 'User Guide'),
                        new GuideGroupItem(href: "http://docs.micronaut.io/${version.versionText}/api/", title: 'API Reference'),
                ])
    }

    private static GuideGroup snapshotDocumentationGuideGroup(String url) {
        new GuideGroup(title: 'Snapshot Documentation',
                image: "${getImageAssetPreffix(url)}documentation.svg",
                items: [

                        new GuideGroupItem(href: "http://docs.micronaut.io/snapshot/guide/index.html", title: 'User Guide'),
                        new GuideGroupItem(href: "http://docs.micronaut.io/snapshot/api/", title: 'API Reference'),

                ])
    }

    @CompileDynamic
    private static List<GuideGroupItem> extraLinksItems(File modules, String siteUrl) {
        Yaml yaml = new Yaml()
        Map model = yaml.load(modules.newDataInputStream())
        model['modules'].collect { k, v ->
            final String url = "https://micronaut-projects.github.io/${v.githubslug}/${v.version}${v.githubslug == 'micronaut-maven-plugin' ? '' : '/guide'}/index.html"
            final String title = "${v.label}${v.version.equalsIgnoreCase('snapshot') ? ' (SNAPSHOT)' : ''}"
            new GuideGroupItem(href: url, title: title, legend: v.legend ?: '', image: getImageAssetPreffix(siteUrl) + v.image, category: v.category)
        }.sort { a, b ->
            a.title.replace("Micronaut", "").replaceAll('^\\s+|\\s+$', "") <=> b.title.replace("Micronaut", "").replaceAll('^\\s+|\\s+$', "")
        }
    }

    private static boolean shouldDisplayPreRelease(File releases) {
        SoftwareVersion preRelease = SiteMap.latestPreReleaseVersion(releases)
        SoftwareVersion latest = SiteMap.latestVersion(releases)
        return preRelease && (preRelease <=> latest) > 0
    }

    @CompileDynamic
    private static void olderVersionBlock(MarkupBuilder html, File releases, String url) {
        html.div(class: "guidegroup") {
            div(class: "guidegroupheader") {
                String title = 'Documentation of older versions'
                String image = "${getImageAssetPreffix(url)}documentation.svg"
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
                        for (String version : SiteMap.olderVersions(releases)) {
                            option version
                        }
                    }
                }
                li {
                    span 'API Reference'
                    select(onchange: "window.location.href='https://docs.micronaut.io/' + this.value + '/api'") {
                        option 'Select a version'
                        for (String version : SiteMap.olderVersions(releases)) {
                            option version
                        }

                    }
                }
            }
        }
    }

}
