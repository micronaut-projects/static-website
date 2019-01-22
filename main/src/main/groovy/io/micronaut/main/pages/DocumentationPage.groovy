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

    List< Map<String, String> > links = [
            [entry: 'micronauttest', label: 'Micronaut Test', version: 'latest', githubslug: 'micronaut-test'],
            [entry: 'mongodb', label: 'Micronaut MongoDB', version: 'latest', githubslug: 'micronaut-mongodb'],
            [entry: 'redis', label: 'Micronaut Redis', version: 'latest', githubslug: 'micronaut-redis'],        
            [entry: 'micronaut-spring', label: 'Micronaut for Spring', version: 'latest', githubslug: 'micronaut-spring'],
            [entry: 'flyway', label: 'Flyway Database Migration', version: 'latest',githubslug: 'micronaut-configuration-flyway'],
            [entry: 'liquibase', label: 'Liquibase Database Migration', version: 'latest',githubslug: 'micronaut-configuration-liquibase'],
            [entry: 'graphql', label: 'Micronaut GraphQL', version: 'snapshot', githubslug: 'micronaut-graphql'],
            [entry: 'grpc', label: 'GRPC', version: 'snapshot', githubslug: 'micronaut-grpc'],
            [entry: 'netflix', label: 'Micronaut Netflix', version: 'latest', githubslug: 'micronaut-netflix'],
            [entry: 'kafka', label: 'Micronaut Kafka', version: 'latest', githubslug: 'micronaut-kafka'],
            [entry: 'micrometer', label: 'Micronaut Micrometer', version: 'latest', githubslug: 'micronaut-micrometer'],        
            [entry: 'groovy', label: 'Micronaut Groovy', version: 'latest', githubslug: 'micronaut-groovy'],
            [entry: 'sql', label: 'Micronaut SQL', version: 'latest', githubslug: 'micronaut-sql'],
            [entry: 'liquibase', label: 'Liquibase Configuration', version: 'latest', githubslug: 'micronaut-configuration-liquibase'],
            [entry: 'flyway', label: 'Flyway Configuration', version: 'latest', githubslug: 'micronaut-configuration-flyway'],
            [entry: 'elasticsearch', label: 'ElasticSearch Configuration', version: 'latest', githubslug: 'micronaut-configuration-elasticsearch'],            
    ] as List< Map<String, String> >

    List<GuideGroupItem> extraLinksItems() {
        links.collect { Map<String, String> link ->

            String url = "https://micronaut-projects.github.io/${link.githubslug}/${link.version}/guide/index.html"
            new GuideGroupItem(href: url, title: "${link.label}${link.version.equalsIgnoreCase('snapshot') ? ' (SNAPSHOT)' : ''}")
        }
    } 

    @Override
    MenuItem menuItem() {
        Navigation.documentationMenuItem(micronautUrl())
    }

    GuideGroup latestDocumentationGuideGroup() {
        new GuideGroup(title: 'Latest Version Documentation',
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
        html.div(class:"content container") {
            h1 {
                span 'Micronaut'
                b 'Documentation'
            }
            div(class: "twocolumns") {
                div(class: "odd column") {
                    mkp.yieldUnescaped latestDocumentationGuideGroup().renderAsHtml()
                    mkp.yieldUnescaped snapshotDocumentationGuideGroup().renderAsHtml()
                }
                div(class: "column") {
                    mkp.yieldUnescaped documentationGuideGroup('Other Modules', extraLinksItems()).renderAsHtml()
                }
            }
        }
        writer.toString()
    }
}
