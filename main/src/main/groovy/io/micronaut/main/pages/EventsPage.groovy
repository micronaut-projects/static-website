package io.micronaut.main.pages

import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.GuideGroup
import io.micronaut.MenuItem
import io.micronaut.Navigation
import io.micronaut.ReadFileUtils
import io.micronaut.pages.Page

@CompileStatic
class EventsPage extends Page  implements ReadFileUtils {
    String title = 'Events'
    String slug = 'events.html'
    String bodyClass = 'events'
    String pwsurl  = 'https://oci-training.cfapps.io'
    int ociTrainingTrack = 34
    String category = 'Micronaut'
    @Override
    MenuItem menuItem() {
        Navigation.desktopEventsItem(micronautUrl())
    }

    @CompileDynamic
    String trainingHtml() {
        String trainigHtml = ''
        try {
            String json = new URL("${pwsurl}/training?trackId=$ociTrainingTrack").text
            def slurper = new JsonSlurper()
            def result = slurper.parseText(json)
            if (!result) {
                trainigHtml += "<p class=\"trainingvoid\">Currently, we don't have any training offerings available.</p>"
            } else {
                trainigHtml +=  trainingTable(result)
            }
        } catch(Exception e) {
            trainigHtml += '<p class="trainingvoid">Something went wrong while retrieving OCI training offerings.</p>'
        }
        trainigHtml
    }

    @CompileDynamic
    @Override
    String mainContent() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        String requestFormText = readFileContent('micronautpresentationrequestform.html')
        html.div(class:"content container", id: 'ocitraining') {
            h1 {
                span 'Events &'
                b 'Training'
            }
            div(class: "twocolumns") {
                div(class: "odd column training") {
                    String trainigHtml = trainingHtml()
                    html.div(class: "guidegroup", style: '') {
                        div(class: "guidegroupheader") {
                            img src: "${getImageAssetPreffix()}training.svg", alt: 'Training'
                            h2 {
                                html.mkp.yieldUnescaped 'Training'
                            }
                        }
                        html.mkp.yieldUnescaped trainigHtml
                    }
                    if ( requestFormText ) {
                        html.div(class: 'desktop') {
                            mkp.yieldUnescaped requestFormText
                        }
                    }
                }
                div(class: "column training") {
                    String eventsHtml = ''
                    try {
                        String json = new URL("${pwsurl}/events?category=$category").text
                        def slurper = new JsonSlurper()
                        def result = slurper.parseText(json)
                        if (!result) {
                            eventsHtml += "<p class=\"trainingvoid\"><b>Currently, we don\'t have any Micronaut events available.</p>"
                        } else {
                            eventsHtml +=  eventsTable(result);
                        }
                    } catch(Exception e) {
                        eventsHtml += '<p class="trainingerror">Something went wrong while retrieving OCI Events.</p>'
                    }

                    html.div(class: "guidegroup", style: '') {
                        div(class: "guidegroupheader") {
                            img src: "${getImageAssetPreffix()}events.svg", alt: 'Events'
                            h2 {
                                html.mkp.yieldUnescaped 'Events'
                            }
                        }
                        html.mkp.yieldUnescaped eventsHtml
                    }
                    if ( requestFormText ) {
                        html.div(class: 'mobile') {
                            mkp.yieldUnescaped requestFormText
                        }
                    }

                }
            }
        }
        writer.toString()
    }

    @CompileDynamic
    String eventsTable(def data) {
        String msg = '''\
<table>
<thead>
<tr><th>Name</th><th>Date(s)</th><th>Location</th></tr>
</thead>
<tbody>
'''
        for ( int i = 0; i < data.size(); i++ ) {
            msg += '<tr>'
            if (data[i].eventHref) {
                msg += "<td><a href=\"${data[i].eventHref}\">${data[i].eventName}</a></td>"
            } else {
                msg += "<td>${data[i].eventName}</td>"
            }
            msg += "<td>${data[i].eventDate}</td>"
            msg += "<td>${data[i].eventLocation}</td>"
            msg += "</tr>"
        }
        msg += '</tbody>'
        msg += '</table>'
        msg
    }

    @CompileDynamic
    String trainingTable(def data) {
        String msg = '''\
<table>
    <colgroup>
       <col>
       <col>
       <col>
       <col>
    </colgroup>
    <thead>
        <tr><th>Course</th><th>Date(s)</th><th>Instructor(s)</th><th>Hour(s)</th></tr>
    </thead>'
    <tbody>
'''
        for (int i = 0; i < data.size(); i++ ) {
            msg += """\
<tr>
<td><a href=\"${data[i].enrollmentLink}\">${data[i].course}</a></td>
<td>${data[i].dates}</td><td>${data[i].instructors}</td>
<td>${data[i].hours}</td>
</tr>
"""
        }
        msg += '</tbody></table>'
        msg
    }
}
