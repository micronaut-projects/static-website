package io.micronaut.main.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.ReadFileUtils
import io.micronaut.MenuItem
import io.micronaut.TextMenuItem
import io.micronaut.pages.Page

@CompileStatic
class SupportPage extends Page implements ReadFileUtils {
    String title = 'Commercial Support'
    String slug = 'support.html'
    String bodyClass = ''

    @Override
    MenuItem menuItem() {
        new TextMenuItem(href: "${micronautUrl()}/support.html", title: 'Support')
    }

    @CompileDynamic
    @Override
    String mainContent() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'content container') {
            h1 {
                span 'Commercial'
                b 'Support'
            }
            div {
                div(class: "twocolumns") {
                    div(class: "column") {
                        div(class: 'transparent_post') {
                            section {
                                p {
                                    mkp.yieldUnescaped micronaut()
                                    mkp.yield " development is sponsored by: "
                                }
                                a(href: "https://objectcomputing.com/products/micronaut/") {
                                    img src: "images/oci_logo_white.svg", width: "90%", alt: "Object Computing"
                                }
                            }
                            div {
                                h3 class: "columnheader", 'Request Free Consultation', style: 'padding-left: 0; margin-top: 50px;margin-bottom: 0 !important;'
                                String text = readFileContent('freeconsultationform.html')
                                if ( text ) {
                                    mkp.yieldUnescaped text
                                }

                            }
                        }
                    }
                    div(class: "column") {
                        div(class: "post") {
                            section {
                                p {
                                    mkp.yield 'OCI offers flexible, customizable open source support services with direct access to the architects and engineers who develop '
                                    mkp.yieldUnescaped(micronaut())
                                    mkp.yield ' and have spent their careers supporting and maturing other Frameworks, such as Grails.'
                                }
                                p 'The OCI team can assist you with:'
                                ul {
                                    li 'Architecture and design review'
                                    li 'Rapid prototyping, troubleshooting, and debugging'
                                    li 'Customizing, modifying, and extending the product'
                                    li 'Integration assistance and support'
                                    li 'Mentorship and architecture throughout the lifecycle of your project'
                                }
                                p {
                                    a href: 'mailto:info@objectcomputing.com', 'Contact OCI'
                                    mkp.yield ' to learn more.'
                                }
                                p '12140 Woodcrest Executive Drive, Suite 250 Saint Louis, MO 63141, USA'
                                p 'Tel: 01*314*579*0066'
                                p 'Email: info@objectcomputing.com'
                          
                            }
                           
                        }
                    }
                }
            }
        }
        writer.toString()
    }
}
