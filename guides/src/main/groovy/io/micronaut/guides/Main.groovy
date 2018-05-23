package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.WebsiteGenerator
import io.micronaut.guides.model.Category
import io.micronaut.Guide
import io.micronaut.guides.model.Tag
import io.micronaut.guides.pages.GuidesPage
import io.micronaut.pages.SiteMapPage

@CompileStatic
class Main {

    static void main(String[] args) {
        List<Guide> guides = []//GuidesFetcher.fetchGuides()
        guides << new Guide(authors: ['Sergio del Amo'],
                title: 'Micronaut CLI',
                tags: ['cli', 'sdkman'],
                category: 'Micronaut Apprentice',
                publicationDate: Date.parse("dd MMM yyyy", "30 April 2018"))

        guides << new Guide(authors: ['Sergio del Amo'],
                title: 'Creating your first Micronaut App',
                tags: ['netty', 'di'],
                category: 'Micronaut Apprentice',
                publicationDate: Date.parse("dd MMM yyyy", "30 April 2018"))

        guides << new Guide(authors: ['Sergio del Amo'],
                title: 'Built-In endpoints',
                tags: ['endpoint'],
                category: 'Micronaut Apprentice',
                publicationDate: Date.parse("dd MMM yyyy", "30 April 2018"))

        guides << new Guide(authors: ['Sergio del Amo'],
                title: 'Compile-Time HTTP Client with @Client',
                tags: ['client'],
                category: 'Micronaut Apprentice',
                publicationDate: Date.parse("dd MMM yyyy", "30 April 2018"))


        guides << new Guide(authors: ['Sergio del Amo'],
                title: 'Consul Discovery Service',
                tags: ['discover-service', 'consul'],
                category: 'Cloud Services',
                publicationDate: Date.parse("dd MMM yyyy", "30 April 2018"))

        guides << new Guide(authors: ['Sergio del Amo'],
                title: 'Zipkin Distributed tracing system',
                tags: ['zipkin', 'distributed-tracing'],
                category: 'Cloud Services',
                publicationDate: Date.parse("dd MMM yyyy", "30 April 2018"))

        guides << new Guide(authors: ['Sergio del Amo'],
                title: 'Circuit Break and Retry',
                tags: ['circuit-breaker', 'retry'],
                category: 'Cloud Services',
                publicationDate: Date.parse("dd MMM yyyy", "30 April 2018"))

        guides << new Guide(authors: ['Sergio del Amo'],
                title: 'Session Based Authentication',
                tags: ['session'],
                category: 'Security',
                publicationDate: Date.parse("dd MMM yyyy", "30 April 2018"))

        guides << new Guide(authors: ['Sergio del Amo'],
                title: 'JWT Authentication - Bearer Token',
                tags: ['jwt', 'bearer'],
                category: 'Security',
                publicationDate: Date.parse("dd MMM yyyy", "30 April 2018"))

        guides << new Guide(authors: ['Sergio del Amo'],
                title: 'JWT Authentication with Cookie',
                tags: ['cookie'],
                category: 'Security',
                publicationDate: Date.parse("dd MMM yyyy", "30 April 2018"))

        guides << new Guide(authors: ['Sergio del Amo'],
                title: 'JWT Authentication in a services federation',
                tags: ['jwt', 'federation'],
                category: 'Security',
                publicationDate: Date.parse("dd MMM yyyy", "30 April 2018"))

        guides << new Guide(authors: ['Sergio del Amo'],
                title: 'Basic Authentication',
                tags: ['basic-auth'],
                category: 'Security',
                publicationDate: Date.parse("dd MMM yyyy", "30 April 2018"))


        Set<Tag> tags = TagUtils.populateTags(guides)
        List<GuidesPage> pages = []
        pages << new GuidesPage(guides, tags)
        for ( Tag tag : tags ) {
            pages << new GuidesPage(guides, tags, tag)
        }
        for ( Category category : GuidesPage.categories().values() ) {
            pages << new GuidesPage(guides, tags, category)
        }
        String timestamp = WebsiteGenerator.timestamp()
        for (GuidesPage page : pages) {
            page.timestamp = timestamp
            String text = page.html()
            String path = "build/site/"
            if ( page.tag ) {
                path = "build/site/tags/"
            }
            if ( page.category ) {
                path = "build/site/categories/"
            }
            File f = new File("${path}${page.slug}")
            f.text = text
        }

        WebsiteGenerator.copyAssetsWithTimestamp(timestamp)

        List<String> urls = guides.collect { Guide guide ->
            "http://guides.micronaut.io/${guide.name}/guide/index.html" as String
        }
        urls.add('index.html')
        SiteMapPage siteMapPage = new SiteMapPage(urls)
        WebsiteGenerator.savePage(siteMapPage, siteMapPage.slug)
    }

}
