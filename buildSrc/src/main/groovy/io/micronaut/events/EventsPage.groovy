package io.micronaut.events

import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.xml.MarkupBuilder

@CompileDynamic
class EventsPage
{
    static final String pwsurl  = 'https://oci-training.cfapps.io'
    static final int ociTrainingTrack = 34
    static final String category = 'Micronaut'

    static String getImageAssetPreffix(String url) {
        url + '/images/'
    }

    @CompileDynamic
    static String trainingHtml() {
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
    static String mainContent(String url, ClassLoader classLoader) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        String requestFormText = '''
<!--[if lte IE 8]>
<script charset="utf-8" type="text/javascript" src="//js.hsforms.net/forms/v2-legacy.js"></script>
<![endif]-->
<script charset="utf-8" type="text/javascript" src="//js.hsforms.net/forms/v2.js"></script>
<script>
    hbspt.forms.create({
        portalId: "4547412",
        formId: "cac2698f-9270-4c1a-8688-cc2d48ef6a5d"
    });
</script>
'''
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
                            img src: "${getImageAssetPreffix(url)}training.svg", alt: 'Training'
                            h2 {
                                html.mkp.yieldUnescaped 'Training'
                            }
                        }
                        html.mkp.yieldUnescaped trainigHtml
                    }
                    if ( requestFormText ) {
                        html.div(class: 'desktop form') {
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
                            img src: "${getImageAssetPreffix(url)}events.svg", alt: 'Events'
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
    static String eventsTable(def data) {
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
    static String trainingTable(def data) {
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
