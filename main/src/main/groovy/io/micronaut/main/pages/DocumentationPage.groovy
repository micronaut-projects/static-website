package io.micronaut.main.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.GuideGroup
import io.micronaut.GuideGroupItem
import io.micronaut.MenuItem
import io.micronaut.Navigation
import io.micronaut.TextMenuItem
import io.micronaut.main.SiteMap
import io.micronaut.pages.Page

@CompileStatic
class DocumentationPage extends Page {
    String title = 'Documentation'
    String slug = 'documentation.html'
    String bodyClass = 'docs'

    @Override
    MenuItem menuItem() {
        Navigation.documentationMenuItem(micronautUrl())
    }

    GuideGroup documentationGuideGroup() {
        new GuideGroup(title: 'Latest Version Documentation',
                description: 'Comming soon!',
                image: "${getImageAssetPreffix()}documentation.svg",
                items: [
//                        new GuideGroupItem(href: "https://micronaut-projects.github.io/micronaut-core/latest/guide/single.html", title: 'Single Page - User Guide'),
//                        new GuideGroupItem(href: "https://micronaut-projects.github.io/micronaut-core/latest/", title: 'User Guide'),
//                        new GuideGroupItem(href: "https://micronaut-projects.github.io/micronaut-core/latest/api/", title: 'API Reference'),
                ])
    }

    GuideGroup snapshotDocumentationGuideGroup() {
        new GuideGroup(title: 'Snapshot Documentation',
                image: "${getImageAssetPreffix()}documentation.svg",
                items: [

                        new GuideGroupItem(href: "https://micronaut-projects.github.io/micronaut-core/snapshot/guide/index.html", title: 'User Guide'),
                        new GuideGroupItem(href: "https://micronaut-projects.github.io/micronaut-core/snapshot/api/", title: 'API Reference'),

                ])
    }

    @CompileDynamic
    @Override
    String mainContent() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class:"content container") {
            h1 {
                span 'Micronaut'
                b 'Documentation'
            }
            div(class: "twocolumns") {
                div(class: "odd column") {
                    mkp.yieldUnescaped snapshotDocumentationGuideGroup().renderAsHtml()
                }
                div(class: "column") {
                    mkp.yieldUnescaped documentationGuideGroup().renderAsHtml()
                }
            }
        }
        writer.toString()
    }
}
