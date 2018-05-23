package io.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
@CompileStatic
class TextMenuItem implements MenuItem {
    String intro
    String title
    String href
    String cssClass
    String style
}
