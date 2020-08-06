package io.micronaut.guides

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.tags.Tag
import io.micronaut.tags.TagCloud

import java.text.SimpleDateFormat

@CompileStatic
class GuidesPage {

    public static final Integer NUMBER_OF_LATEST_GUIDES = 5
    private static final Integer MARGIN_TOP = 50
    public static final String GUIDES_URL = "https://guides.micronaut.io"

    @CompileDynamic
    static String renderGuide(Guide guide, String query = null) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.li {
            if ( guide instanceof SingleLanguageGuide) {
                a class: 'guide', href: "https://guides.micronaut.io/${guide.name}/guide/index.html", guide.title
                guide.tags.each { String tag ->
                    span(style: 'display: none', class: 'tag', tag)
                }
            } else if (guide instanceof MultiLanguageGuide) {
                MultiLanguageGuide multiLanguageGuide = ((MultiLanguageGuide) guide)
                div(class: 'multiguide') {
                    span(class: 'title', guide.title)
                    for (ProgrammingLanguage lang :  multiLanguageGuide.githubSlugs.keySet())  {
                        Set<String> tagList = multiLanguageGuide.programmingLanguageTags[lang] as Set<String>
                        tagList << lang.toString().toLowerCase()

                        if (query == null || titlesMatchesQuery(guide.title, query) || tagsMatchQuery(tagList as List<String>, query)) {
                            div(class: 'align-left') {
                                a(class: 'lang', href: "https://guides.micronaut.io/${multiLanguageGuide.githubSlugs[lang].replaceAll('micronaut-guides/', '')}/guide/index.html") {
                                    mkp.yield(lang.name())
                                }
                                tagList.each { String tag ->
                                    span(style: 'display: none', class: 'tag', tag)
                                }
                            }
                        }
                    }
                }
            }

        }
        writer.toString()
    }

    static boolean titlesMatchesQuery(String title, String query) {
        title.indexOf(query) != -1
    }

    static boolean tagsMatchQuery(List<String> tags, String query) {
        tags.any { it.indexOf(query) != -1 }
    }

    @CompileDynamic
    static String guideGroupByCategory(String url,
                                Category category,
                                List<Guide> guides,
                                boolean linkToCategory = true,
                                String cssStyle = '') {
        List<Guide> categoryGuides = guides.findAll { it.category == category.name }
        if ( !categoryGuides ) {
            return ''
        }
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)

        html.div(class: "guidegroup", style: cssStyle) {
            div(class: "guidegroupheader") {
                img src: "${getImageAssetPreffix(url)}${category.image}" as String, alt: category.name
                if ( linkToCategory )  {
                    a(href: "${url}/categories/${category.slug}.html") {
                        h2 category.name
                    }
                } else {
                    h2 category.name
                }
            }
            ul {
                categoryGuides.each { mkp.yieldUnescaped renderGuide(it) }
            }
        }
        writer.toString()
    }

    @CompileDynamic
    static String guideGroupByTag(Tag tag, List<Guide> guides, String url) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)

        html.div(class: "guidegroup") {
            div(class: "guidegroupheader") {
                img src: "${getImageAssetPreffix(url)}documentation.svg" as String, alt: 'Guides'
                h2 "Guides filtered by #${tag.title}"
            }
            ul {
                List<Guide> tagGuides = guides.findAll { Guide guide -> guide.tags.contains(tag.title) }
                tagGuides.each { mkp.yieldUnescaped renderGuide(it, tag.title) }
            }
        }
        writer.toString()
    }

    static String getImageAssetPreffix(String url) {
        url + '/images/'
    }

    @CompileDynamic
    static String searchBox(String id) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'searchbox') {
            div(class: 'search', style: 'margin-bottom: 0px !important;') {
                input(type: 'text', id: id, placeholder: 'SEARCH')
            }
        }
        writer.toString()
    }

    @CompileDynamic
    static String sponsoredBy(String url) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'sponsoredby', style: 'margin-top: 50px;') {
            h4 'Sponsored by'
            a(href: 'https://objectcomputing.com/products/micronaut/') {
                img src: "${getImageAssetPreffix(url)}oci-home-to-micronaut.svg", alt: 'Object Computing', width: '250px'
            }
        }
        writer.toString()
    }

    @CompileDynamic
    static String latestGuides(List<Guide> guides) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'latestguides') {
            h3 class: 'columnheader', 'Latest Guides'
            ul {
                List<Guide> latestGuides = guides.findAll {
                    it.publicationDate
                }.sort { Guide a, Guide b ->
                    b.publicationDate <=> a.publicationDate
                }.take(NUMBER_OF_LATEST_GUIDES)
                latestGuides.each { Guide guide ->
                    li {
                        b guide.title
                        span {
                            mkp.yield new SimpleDateFormat('MMM dd, yyyy').format(guide.publicationDate)

                            mkp.yield ' - '
                            mkp.yield guide.category
                        }
                        if (guide instanceof MultiLanguageGuide) {
                            MultiLanguageGuide multiLanguageGuide = ((MultiLanguageGuide) guide)
                            span guide.title
                            for (ProgrammingLanguage lang : multiLanguageGuide.githubSlugs.keySet()) {
                                a(style: 'display: inline;', class: 'lang', href: "https://guides.micronaut.io/${multiLanguageGuide.githubSlugs[lang].replaceAll('micronaut-guides/', '')}/guide/index.html") {
                                    mkp.yield(lang.name())
                                }
                            }
                        } else {
                            a href: "https://guides.micronaut.io/${guide.name}/guide/index.html", 'Read More'
                        }
                    }
                }
            }
        }
        writer.toString()
    }

    static String guidesUrl(String url) {
        url == 'https://micronaut.io' ? GUIDES_URL : url
    }

    @CompileDynamic
    static String mainContent(String mainUrl,
                              List<Guide> guides,
                              Set<Tag> tags,
                              Category category = null,
                              Tag tag = null) {
        String url = guidesUrl(mainUrl)
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.article {
            div(class: 'content container') {
                h1 {
                    span {
                        mkp.yieldUnescaped 'Micronaut<sup>&reg;</sup>'
                    }
                    b 'Guides'
                }
                setOmitEmptyAttributes(true)
                setOmitNullAttributes(true)
                String guideGroupCss = "margin-top: ${MARGIN_TOP}px;"
                div(class: 'twocolumns') {
                    div(class: 'column') {
                        if ( !(tag || category) ) {
                            div(class: 'mobile', style: 'margin-bottom: 50px;') {
                                mkp.yieldUnescaped searchBox('mobilequery')
                            }
                        }
                        div(id: 'searchresults') {
                            mkp.yieldUnescaped('')
                        }
                        if ( !(tag || category) ) {
                            mkp.yieldUnescaped latestGuides(guides)
                        }
                        mkp.yieldUnescaped sponsoredBy(url)
                        if ( !(tag || category) ) {
                            mkp.yieldUnescaped TagCloud.tagCloud(url + "/tags/", tags)
                            mkp.yieldUnescaped guideGroupByCategory(url, categories().cache, guides, true, guideGroupCss)
                            mkp.yieldUnescaped guideGroupByCategory(url, categories().messaging, guides, true, guideGroupCss)
                            mkp.yieldUnescaped guideGroupByCategory(url, categories().security, guides, true, guideGroupCss)
                        }
                    }
                    div(class: 'column') {
                        if ( !(tag || category) ) {
                            div(class: 'desktop') {
                                mkp.yieldUnescaped searchBox('query')
                            }
                        }
                        if ( tag ) {
                            mkp.yieldUnescaped guideGroupByTag(tag, guides, url)

                        } else if ( category ) {
                            mkp.yieldUnescaped guideGroupByCategory(url, category, guides.findAll { it.category == category.name }, false )

                        }
                        if ( !(tag || category) ) {
                            mkp.yieldUnescaped guideGroupByCategory(url, categories().apprentice, guides, true, guideGroupCss)
                            mkp.yieldUnescaped guideGroupByCategory(url, categories().aws, guides, true, guideGroupCss)
                            mkp.yieldUnescaped guideGroupByCategory(url, categories().azure, guides, true, guideGroupCss)
                            mkp.yieldUnescaped guideGroupByCategory(url, categories().googlecloud, guides, true, guideGroupCss)
                            mkp.yieldUnescaped guideGroupByCategory(url, categories().tracing, guides, true, guideGroupCss)
                            mkp.yieldUnescaped guideGroupByCategory(url, categories().servicediscovery, guides, true, guideGroupCss)
                            mkp.yieldUnescaped guideGroupByCategory(url, categories().cloudservices, guides, true, guideGroupCss)
                            mkp.yieldUnescaped guideGroupByCategory(url, categories().dataaccess, guides, true, guideGroupCss)
                        }
                    }
                }
            }
        }
        writer.toString()
    }

    static Map<String, Category> categories() {
        [
                servicediscovery: new Category(name: "Service Discovery", image: 'service-discovery.svg'),
                tracing: new Category(name: "Distributed Tracing", image: 'tracing.svg'),
                messaging: new Category(name: "Messaging", image: 'messaging.svg'),
                aws: new Category(name: "Micronaut + AWS", image: 'aws.svg'),
                azure: new Category(name: "Micronaut + Microsoft Azure", image: 'azure.svg'),
                googlecloud: new Category(name: "Micronaut + Google Cloud", image: 'googlecloud.svg'),
                android: new Category(name: "Micronaut Android", image: 'micronaut_android.svg'),
                devops: new Category(name: "Micronaut DevOps", image: 'micronaut_devops.svg'),
                apprentice: new Category(name: "Micronaut Apprentice", image: 'micronautaprrentice.svg'),
                cloudservices: new Category(name: 'Cloud Native', image: 'cloud.svg'),
                security: new Category(name: 'Micronaut Security', image: 'security.svg'),
                dataaccess: new Category(name: 'Data Access', image: 'dataaccess.svg'),
                cache: new Category(name: 'Cache', image: 'cache.svg'),
        ]
    }
}
