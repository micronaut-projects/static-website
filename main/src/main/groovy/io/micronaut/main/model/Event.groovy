package io.micronaut.main.model

import groovy.transform.CompileStatic

@CompileStatic
class Event implements EventHtml {
    String image
    String href
    String title
    String location
    String dates
    String about
}
