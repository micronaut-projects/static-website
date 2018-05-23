package io.micronaut

import groovy.transform.CompileDynamic
import groovy.xml.MarkupBuilder

class Training {
    @CompileDynamic
    static String training() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'training guidegroup', style: 'display: none;') {
            div(class: "guidegroupheader") {
                img src: image, alt: title
                h2 {
                    a(href: 'https://objectcomputing.com/training/catalog/micronaut-training', 'Micronaut Training')
                }
            }
            div(id: 'ocitraining') {
                span ''
            }
        }
        writer.toString()
    }
}
