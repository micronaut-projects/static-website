package io.micronaut

import groovy.transform.CompileDynamic
import groovy.xml.MarkupBuilder

class Events {
    @CompileDynamic
    static String events() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'training', style: 'display: none;') {
            h3(class: 'columnheader') {
                a(href: 'https://objectcomputing.com/resources/events', 'OCI Events')
            }
            div(id: 'ocievents') {
                span ''
            }
        }
        writer.toString()
    }
}
