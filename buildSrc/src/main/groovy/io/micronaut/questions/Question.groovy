package io.micronaut.questions
import groovy.transform.CompileStatic

@CompileStatic
class Question implements QuestionHtml {
    String title
    String answer
    String slug
}
