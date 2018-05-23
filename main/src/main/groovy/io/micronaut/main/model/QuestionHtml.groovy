package io.micronaut.main.model

import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.main.MarkdownUtil
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
                h2(class: 'columnheader') {
                    html.mkp.yieldUnescaped title
                }
                html.mkp.yieldUnescaped(MarkdownUtil.htmlFromMarkdown(answer))
        }
        writer.toString()
    }

}
