package io.micronaut.main.model

import groovy.transform.CompileStatic

@CompileStatic
class DocumentationLink implements DocumentationLinkHtml {
    String title
    String href
    String vcs
}
