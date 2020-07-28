package io.micronaut.gallery

import groovy.transform.CompileDynamic
import groovy.xml.MarkupBuilder
import org.yaml.snakeyaml.Yaml

class MicronautLogosPage {
    @CompileDynamic
    static String mainContent(File questions) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        Yaml yaml = new Yaml()
        Map model = yaml.load(questions.newDataInputStream())
        List<MicronautLogo> logos = model['logos'].collect {
            new MicronautLogo(label: it['label'], asset: it['asset'], background: it['background'])
        }

        html.div(id: "micronaut-logos-page", class:'content container') {
            h1 {
                span 'Micronaut'
                b 'Logos'
            }
            div(class: 'light padded') {
                div(class: 'heading') {
                    h2 "WELCOME TO THE MICRONAUT LOGO GALLERY"
                    p {
                        span "In all new productions and printings of your products and related marketing materials, use the logo that correctly represents your product’s qualification. Any use of this logo on websites and/or marketing materials must be authorized by the Micronaut brand team. Contact us by emailing"
                        a("info@micronaut.io", href:"mailto:info@micronaut.io")
                    }
                }
                div(class: 'logos') {
                    for( MicronautLogo logo : logos) {
                        mkp.yieldUnescaped logo.renderAsHtml()
                    }
                }
            }
        }
        writer.toString()
    }
}
