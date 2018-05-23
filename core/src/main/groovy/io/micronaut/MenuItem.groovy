package io.micronaut

import groovy.transform.CompileStatic

@CompileStatic
interface MenuItem {
    String getHref()
    String getCssClass()
    String getStyle()
}