package io.micronaut

import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.data.DataHolder
import com.vladsch.flexmark.util.data.MutableDataSet

class MarkdownUtil {
    static DataHolder OPTIONS = new MutableDataSet().set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()))
            // set GitHub table parsing options
            .set(TablesExtension.WITH_CAPTION, false)
            .set(TablesExtension.COLUMN_SPANS, false)
            .set(TablesExtension.MIN_HEADER_ROWS, 1)
            .set(TablesExtension.MAX_HEADER_ROWS, 1)
            .set(TablesExtension.APPEND_MISSING_COLUMNS, true)
            .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
            .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)
            .toImmutable()
    static Parser PARSER = Parser.builder(OPTIONS).build()
    static HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build()

    static String htmlFromMarkdown(String answer) {
        Node document = PARSER.parse(answer)
         RENDERER.render(document)
    }
}
