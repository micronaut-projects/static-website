package io.micronaut.gallery

import groovy.transform.CompileDynamic
import groovy.xml.MarkupBuilder
import org.yaml.snakeyaml.Yaml

class MicronautLogosPage {
    @CompileDynamic
    static String mainContent(File logosYaml) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        Yaml yaml = new Yaml()
        Map model = yaml.load(logosYaml.newDataInputStream())
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
                        span "Micronaut logos and design marks are proprietary assets owned by Object Computing, Inc. We ask that you refrain from using Micronaut logos, including the Micronaut swirl icon and Micronaut astronaut (\"Sally\"), unless you are specifically licensed or authorized to use them by the Micronaut brand team. Please contact us by emailing"
                        a("info@micronaut.io", href:"mailto:info@micronaut.io?subject=Micronaut Logo Usage Request")
                        span "for more information or to request authorization."
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
