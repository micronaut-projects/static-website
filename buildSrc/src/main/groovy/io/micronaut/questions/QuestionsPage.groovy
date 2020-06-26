package io.micronaut.questions

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import org.yaml.snakeyaml.Yaml

@CompileStatic
class QuestionsPage {
    @CompileDynamic
    static String mainContent(File questions) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        Yaml yaml = new Yaml()
        Map model = yaml.load(questions.newDataInputStream())
        List<Question> questionList = model['questions'].collect {
            new Question(title: it['title'], answer: it['answer'], slug: it['slug'])
        }
        html.div(class:'content container') {
            h1 {
                span 'Frequently Asked'
                b 'Questions'
            }
            div(class: 'light') {
                article(class: 'padded') {
                    for( Question question : questionList) {
                        mkp.yieldUnescaped question.renderAsHtml()
                    }
                }
            }
        }
        writer.toString()
    }
}
