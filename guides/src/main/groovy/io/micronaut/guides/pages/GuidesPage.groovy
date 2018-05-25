package io.micronaut.guides.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.Navigation
import io.micronaut.ReadFileUtils
import io.micronaut.guides.model.Category
import io.micronaut.Guide
import io.micronaut.guides.model.Tag
import io.micronaut.TextMenuItem
import io.micronaut.Training
import io.micronaut.pages.Page

@CompileStatic
class GuidesPage extends Page implements ReadFileUtils {

    public static final Integer NUMBER_OF_LATEST_GUIDES = 5
    private static final Integer MARGIN_TOP = 50

    String bodyClass = 'guides'
    List<Guide> guides
    Set<Tag> tags
    Tag tag
    Category category

    GuidesPage(List<Guide> guides, Set<Tag> tags) {
        this.guides = guides
        this.tags = tags
    }

    GuidesPage(List<Guide> guides, Set<Tag> tags, Tag tag) {
        this(guides, tags)
        this.tag = tag
    }

    GuidesPage(List<Guide> guides, Set<Tag> tags, Category category) {
        this(guides, tags)
        this.category = category
    }

    String getSlug() {
        if ( tag ) {
            return "${tag.slug.toLowerCase()}.html"
        }
        if ( category ) {
            return "${category.slug.toLowerCase()}.html"
        }
        'index.html'
    }

    @Override
    boolean doNotIndex() {
        if ( tag || category ) {
            return true
        }
        false
    }

    @Override
    String getHtmlHeadTitle() {
        'Guides | Micronaut Framework'
    }

    @CompileDynamic
    String renderGuide(Guide guide) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.li {
            a class: 'guide', href: "http://guides.micronaut.io/${guide.name}/guide/index.html", guide.title
            guide.tags.each { String tag ->
                span(style: 'display: none', class: 'tag', tag)
            }
        }
        writer.toString()
    }


    @CompileDynamic
    @Override
    String getTitle() {
        if ( tag || category ) {
            StringWriter writer = new StringWriter()
            MarkupBuilder html = new MarkupBuilder(writer)
            html.div {
                a href: "${guidesUrl()}/index.html", 'Guides'
                if (tag) {
                    mkp.yieldUnescaped " &rarr; #${tag.title}"
                } else if (category) {
                    mkp.yieldUnescaped " &rarr; ${category.name}"
                }
            }
            return writer.toString()
        }
        'Guides'
    }

    @Override
    List<String> getJavascriptFiles() {
        List<String> jsFiles = super.getJavascriptFiles()
        jsFiles << ("${guidesUrl()}/javascripts/${timestamp ? (timestamp + '.') : ''}search.js" as String)
        jsFiles
    }

    @Override
    List<String> getCssFiles() {
        ["${guidesUrl()}/stylesheets/${timestamp ? (timestamp + '.') : ''}screen.css" as String]
    }

    @Override
    String getImageAssetPreffix() {
        "${guidesUrl()}/images/"
    }

    @Override
    TextMenuItem menuItem() {
        if ( tag ||category ){
            return null
        }
        Navigation.guidesMenuItem(guidesUrl())
    }

    @CompileDynamic
    String guideGroupByCategory(Category category, List<Guide> guides, boolean linkToCategory = true, String cssStyle = '') {
        List<Guide> categoryGuides = guides.findAll { it.category == category.name }
        if ( !categoryGuides ) {
            return ''
        }
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)

        html.div(class: "guidegroup", style: cssStyle) {
            div(class: "guidegroupheader") {
                img src: "${getImageAssetPreffix()}${category.image}" as String, alt: category.name
                if ( linkToCategory )  {
                    a(href: "${guidesUrl()}/categories/${category.slug}.html") {
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
    String guideGroupByTag(Tag tag, List<Guide> guides) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)

        html.div(class: "guidegroup") {
            div(class: "guidegroupheader") {
                img src: "${getImageAssetPreffix()}documentation.svg" as String, alt: 'Guides'
                h2 "Guides filtered by #${tag.title}"
            }
            ul {
                List<Guide> tagGuides = guides.findAll { Guide guide -> guide.tags.contains(tag.title) }
                tagGuides.each { mkp.yieldUnescaped renderGuide(it) }
            }
        }
        writer.toString()
    }

    @CompileDynamic
    String guideSuggestion() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'guidesuggestion') {
            h3 class: 'columnheader', 'Which topic would you like us to cover?'
            String formHtml = readFileContent('guidesuggestionform.html')
            if ( formHtml ) {
                mkp.yieldUnescaped formHtml
            }
        }
        writer.toString()
    }

    @CompileDynamic
    String searchBox(String id) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        if ( !(tag || category) ) {
            html.div(class: 'searchbox') {
                div(class: 'search', style: 'margin-bottom: 0px !important;') {
                    input(type: 'text', id: id, placeholder: 'SEARCH')
                }
            }
        }
        writer.toString()
    }

    @CompileDynamic
    String sponsoredBy() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'sponsoredby', style: 'margin-top: 50px;') {
            h4 'Sponsored by'
            a(href: 'https://objectcomputing.com/products/micronaut/') {
                img src: "${getImageAssetPreffix()}oci_logo_white.svg", alt: 'Object Computing'
            }
        }
        writer.toString()
    }

    @CompileDynamic
    String latestGuides() {
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
                            mkp.yield guide.publicationDate.format('MMM dd, yyyy')
                            mkp.yield ' - '
                            mkp.yield guide.category
                        }
                        a href: "http://guides.micronaut.io/${guide.name}/guide/index.html", 'Read More'
                    }
                }
            }
        }
        writer.toString()
    }

    @CompileDynamic
    String tagCloud() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'tagsbytopic') {
            h3 class: 'columnheader', 'Guides by Tag'
            ul(class: 'tagcloud') {
                tags.sort { Tag a, Tag b -> a.slug <=> b.slug }.each { Tag t ->
                    li(class: "tag${t.ocurrence}") {
                        a href: "${guidesUrl()}/tags/${t.slug.toLowerCase()}.html", t.title
                    }
                }
            }
        }
        writer.toString()
    }

    @CompileDynamic
    String mainContent() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.article {
            div(class: 'content container') {
                h1 {
                    span 'Micronaut'
                    b 'Guides'
                }
                setOmitEmptyAttributes(true)
                setOmitNullAttributes(true)
                div(class: 'twocolumns') {
                    div(class: 'column') {
                        div(class: 'mobile', style: 'margin-bottom: 50px;') {
                            mkp.yieldUnescaped searchBox('mobilequery')
                        }
                        div(id: 'searchresults') {
                            mkp.yieldUnescaped('')
                        }
                        mkp.yieldUnescaped latestGuides()
                        mkp.yieldUnescaped guideGroupByCategory(categories().apprentice, guides)
                    }
                    div(class: 'column') {
                        div(class: 'desktop') {
                            mkp.yieldUnescaped searchBox('query')
                        }
                        if ( tag || category ) {
                            mkp.yieldUnescaped sponsoredBy()
                        } else {
                            mkp.yieldUnescaped sponsoredBy()
                            mkp.yieldUnescaped tagCloud()
                        }

                        if ( tag ) {
                            mkp.yieldUnescaped guideGroupByTag(tag, guides)

                        } else if ( category ) {
                            mkp.yieldUnescaped guideGroupByCategory(category, guides.findAll { it.category == category.name }, false )

                        } else {
                            mkp.yieldUnescaped guideGroupByCategory(categories().security, guides, true, "margin-top: ${MARGIN_TOP};")
                        }
                    }
                }
//                div(class: 'twocolumns') {
//                    div(class: 'column') {
//                        if ( !(tag || category) ) {
//                            mkp.yieldUnescaped guideGroupByCategory(categories().cloudservices, guides, true, "margin-top: ${MARGIN_TOP};")
//                        }
//                    }
//                    div(class: 'column') {
//                        if ( !(tag || category) ) {
////                        mkp.yieldUnescaped guideSuggestion()
//                        }
//                    }
//                }
            }
        }
        writer.toString()
    }

    static Map<String, Category> categories() {
        [
            android: new Category(name: "Micronaut Android", image: 'micronaut_android.svg'),
            devops: new Category(name: "Micronaut DevOps", image: 'micronaut_devops.svg'),
            apprentice: new Category(name: "Micronaut Apprentice", image: 'micronautaprrentice.svg'),
            cloudservices: new Category(name: 'Cloud Services', image: 'cloud.svg'),
            security: new Category(name: 'Micronaut Security', image: 'security.svg'),
        ]
    }
}
