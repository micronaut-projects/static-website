package io.micronaut.questions

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.MarkdownUtil
import io.micronaut.PageElement

@CompileStatic
trait QuestionHtml implements PageElement {

    abstract String getTitle()
    abstract String getAnswer()
    abstract String getSlug()

    @CompileDynamic
    @Override
    String renderAsHtml() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'question', id: slug) {
            h2 {
                html.mkp.yieldUnescaped title
            }
            html.mkp.yieldUnescaped(MarkdownUtil.htmlFromMarkdown(answer))
        }
        writer.toString()
    }

}