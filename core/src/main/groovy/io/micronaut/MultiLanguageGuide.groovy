package io.micronaut

import groovy.transform.CompileStatic

@CompileStatic
class MultiLanguageGuide implements Guide {
    List<String> authors
    String category
    Map<ProgrammingLanguage, String> githubSlugs
    String name
    String title
    String subtitle
    List<String> tags
    Date publicationDate
}
