package io.micronaut.guides

import groovy.transform.CompileStatic

@CompileStatic
enum ProgrammingLanguage {
    JAVA(1),
    KOTLIN(2),
    GROOVY(3)

    final int id
    private ProgrammingLanguage(int id) { this.id = id }
}
