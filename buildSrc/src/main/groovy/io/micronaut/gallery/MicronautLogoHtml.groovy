package io.micronaut.gallery

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.PageElement

@CompileStatic
trait MicronautLogoHtml implements PageElement {
    abstract String getThumb()
    abstract String getSvg()
    abstract String getPng()
    abstract String getLabel()
    abstract String getBackground();

    @CompileDynamic
    @Override
    String renderAsHtml() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'logo') {
            if (thumb) {
                div(class: 'logo-wrapper', style: "background-color: ${background};" ) {
                    img(src: thumb, alt: label)
                }
            }
            if (png || svg) {
                span 'Full Resolution: '
                if (png) {
                    a href: png, 'PNG'
                }
                if (svg) {
                    a href: svg, 'SVG'
                }
            }
        }
        writer.toString()
    }
}