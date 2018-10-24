package io.micronaut

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
interface Guide {
    String getVersionNumber()
    List<String> getAuthors()
    String getCategory()
    String getName()
    String getTitle()
    String getSubtitle()
    List<String> getTags()
    Date getPublicationDate()
}
