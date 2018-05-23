package io.micronaut.main.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.Events
import io.micronaut.GuideGroup
import io.micronaut.GuideGroupItem
import io.micronaut.MenuItem
import io.micronaut.Navigation
import io.micronaut.ReadFileUtils
import io.micronaut.Training
import io.micronaut.main.SiteMap
import io.micronaut.pages.Page

@CompileStatic
class EventsPage extends Page  implements ReadFileUtils {
    String title = 'Events'
    String slug = 'events.html'
    String bodyClass = 'events'

    @Override
    MenuItem menuItem() {
        Navigation.desktopEventsItem(micronautUrl())
    }

    @Override
    List<String> getJavascriptFiles() {
        List<String> jsFiles = super.getJavascriptFiles()
        jsFiles << ("${micronautUrl()}/javascripts/oci-training.js" as String)
        jsFiles
    }

    @CompileDynamic
    @Override
    String mainContent() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class:"content container") {
            h1 {
                span 'Events &'
                b 'Training'
            }
            div(class: "twocolumns") {
                div(class: "odd column training") {
                    mkp.yieldUnescaped trainingGuideGroup().renderAsHtml()

                    p 'Training and events developed and delivered by the Micronaut founders and core development team.'

                    String text = readFileContent('micronautpresentationrequestform.html')
                    if ( text ) {
                        mkp.yieldUnescaped text
                    }

//
//                    ul(class: "iconsgrid") {
//                        li(style: 'margin-bottom: 0;') {
//                            img src: "${imageAssetPreffix}circle_graemerocher.png", alt: "Graeme Rocher"
//                            span 'Graeme Rocher'
//                        }
//                        li(style: 'margin-bottom: 0;') {
//                            img src: "${imageAssetPreffix}circle_jeffscottbrown.png", alt: "Jeff Scott Brown"
//                            span 'Jeff Scott Brown'
//
//                        }
//                        li(style: 'margin-bottom: 0;') {
//                            img src: "${imageAssetPreffix}circle_james.png", alt: "James Kleeh"
//                            span 'James Kleeh'
//                        }
//                        li(style: 'margin-bottom: 0;') {
//                            img src: "${imageAssetPreffix}circle_ryan.png", alt: "Ryan Vanderwerf"
//                            span 'Ryan Vanderwerf'
//                        }
//                        li(style: 'margin-bottom: 0;') {
//                            img src: "${imageAssetPreffix}circle_alvarosanchez.png", alt: "Álvaro Sanchez-Mariscal"
//                            span 'Álvaro Sanchez-Mariscal'
//                        }
//                    }
                }
                div(class: "column training") {
                    mkp.yieldUnescaped eventsGuideGroup().renderAsHtml()

                }
            }
        }
        writer.toString()
    }

    GuideGroup eventsGuideGroup() {
        new GuideGroup(title: 'Events',
                image: "${getImageAssetPreffix()}events.svg",
                ulId: 'ocievents',
                items: [])
    }

    GuideGroup trainingGuideGroup() {
        new GuideGroup(title: 'Training',
                image: "${getImageAssetPreffix()}training.svg",
                ulId: 'ocitraining',
                items: [])
    }
}
