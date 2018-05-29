package io.micronaut.main.model

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.PageElement

@CompileStatic
trait BuildStatusHtml implements PageElement {

    abstract String getTitle()
    abstract String getHref()
    abstract String getBadge()

    @CompileDynamic
    @Override
    String renderAsHtml() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.tr {
            td title
            td {
                a(href: href, class: 'align-right') {
                    img src: badge
                }
            }
        }
        writer.toString()
    }


}