package io.micronaut.events


import groovy.transform.CompileDynamic
import groovy.xml.MarkupBuilder
import io.micronaut.questions.Question
import org.yaml.snakeyaml.Yaml

@CompileDynamic
class EventsPage {

    @CompileDynamic
    static String mainContent(List<Event> events) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)

        html.div(class:'content container') {
            h1 {
                span {
                    mkp.yieldUnescaped 'Micronaut<sup>&reg;</sup>'
                }
                b 'Events'
            }
            div(class: 'light') {
                article(class: 'padded') {
                    mkp.yieldUnescaped eventsTable(events)
                }
            }
        }
        writer.toString()
    }

    @CompileDynamic
    static String eventsTable(List<Event> events) {
        String msg = '''\
<table>
<thead>
<tr><th>Event</th><th>Date(s)</th><th>Location</th><th>Speakers</th></tr>
</thead>
<tbody>
'''
        for (Event event : events) {
            msg += tableRowForEvent(event)
        }
        msg += '</tbody>'
        msg += '</table>'
        msg
    }

    static String tableRowForEvent(Event event) {
        String msg = '<tr>'
        if (event.link) {
            msg += "<td><a href=\"${event.link}\">${event.name ?: ''}</a></td>"
        } else {
            msg += "<td>${event.name ?: ''}</td>"
        }
        msg += "<td>${event.date ?: ''}</td>"
        msg += "<td>${event.location ?: ''}</td>"
        msg += "<td>${(event.speakers ? event.speakers.join(', ') : '')}</td>"
        msg += "</tr>"
        msg
    }

}
