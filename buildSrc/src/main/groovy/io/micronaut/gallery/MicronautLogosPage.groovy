package io.micronaut.gallery

import groovy.transform.CompileDynamic
import groovy.xml.MarkupBuilder

class MicronautLogosPage {
    @CompileDynamic
    static String mainContent(List<MicronautLogo> logos) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class:'content container') {
            h1 {
                span {
                    mkp.yieldUnescaped 'Micronaut<sup>&reg;</sup>'
                }
                b 'Logos'
            }
            div(class: 'light padded') {
                p {
                    mkp.yield"Micronaut logos and design marks are proprietary assets owned by Object Computing, Inc. We ask that you refrain from using Micronaut logos, including the Micronaut swirl icon and Micronaut astronaut (\"Sally\"), unless you are specifically licensed or authorized to use them by the Micronaut brand team. Please contact us by emailing"
                    a("info@micronaut.io", href:"mailto:info@micronaut.io?subject=Micronaut Logo Usage Request")
                    mkp.yield " for more information or to request authorization."
                }
                div(class: 'logos') {
                    int count = 0
                    for (MicronautLogo logo : logos) {
                        if (count == 0) {
                            mkp.yieldUnescaped "<div class='twocolumns'>"
                        }
                        div(class: "column") {
                            mkp.yieldUnescaped logo.renderAsHtml()
                        }
                        if (count == 1) {
                            mkp.yieldUnescaped "</div>"
                            count = 0
                        } else {
                           count++
                        }
                    }
                }
            }
        }
        writer.toString()
    }



}
