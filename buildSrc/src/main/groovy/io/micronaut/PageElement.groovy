package io.micronaut

import groovy.transform.CompileStatic

@CompileStatic
interface PageElement {

    String renderAsHtml()
}
