package io.micronaut.main.model

import groovy.transform.CompileDynamic
import groovy.xml.MarkupBuilder
import io.micronaut.PageElement

trait ProfileHtml implements PageElement {

    abstract String getHref()

    abstract String getTitle()

    @CompileDynamic
    String renderAsHtml() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.li {
            a href: href, title
        }
        writer.toString()
    }
}
