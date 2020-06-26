package io.micronaut

import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.data.DataHolder
import com.vladsch.flexmark.util.data.MutableDataSet

class MarkdownUtil {
    static DataHolder OPTIONS = new MutableDataSet().set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create())).toImmutable()
    static Parser PARSER = Parser.builder(OPTIONS).build()
    static HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build()

    static String htmlFromMarkdown(String answer) {
        Node document = PARSER.parse(answer)
         RENDERER.render(document)
    }
}
