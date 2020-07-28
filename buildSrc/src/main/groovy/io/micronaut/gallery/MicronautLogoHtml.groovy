package io.micronaut.gallery

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.PageElement

@CompileStatic
trait MicronautLogoHtml implements PageElement {

    abstract String getSrc()
    abstract String getDownload()
    abstract String getLabel()
    abstract String getBackground();
    abstract String getAsset()

    @CompileDynamic
    @Override
    String renderAsHtml() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'logo') {
            div(class: 'logo-wrapper', style: "background-color: ${background};" ) {
                    img(src: src, alt: label)
                }
            a href: download, 'Get the full resolution version here.', download: asset
        }
        writer.toString()
    }
}