package io.micronaut.main.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.main.SiteMap
import io.micronaut.MenuItem
import io.micronaut.TextMenuItem
import io.micronaut.main.model.Question
import io.micronaut.pages.Page

@CompileStatic
class QuestionPage extends Page {
    String title = 'Questions'
    String slug  = 'faq.html'
    String bodyClass = 'faq'

    @Override
    MenuItem menuItem() {
        new TextMenuItem(href: "${micronautUrl()}/faq.html", title: 'FAQ')
    }

    @CompileDynamic
    @Override
    String mainContent() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        List<Question> questionList = SiteMap.QUESTIONS
        html.div(class: 'content') {
            article(id: 'questions',class: 'container') {
                h1 {
                    span 'Frequently Asked'
                    b 'Questions'
                }
                for( Question question : questionList) {
                    mkp.yieldUnescaped question.renderAsHtml()
                }
            }
        }
        writer.toString()
    }
}
