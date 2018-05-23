package io.micronaut

import groovy.transform.CompileStatic

@CompileStatic
class IconMenuItem implements MenuItem {
    String image
    String href
    String alt
    String cssClass
    String style
}
