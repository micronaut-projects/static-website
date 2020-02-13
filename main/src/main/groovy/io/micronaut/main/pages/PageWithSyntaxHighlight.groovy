package io.micronaut.main.pages

import groovy.transform.CompileStatic
import io.micronaut.pages.Page

@CompileStatic
abstract class PageWithSyntaxHighlight extends Page {
    @Override
    List<String> getCssFiles() {
        List<String> l = super.getCssFiles()
        String prismjs = "stylesheets/${timestamp ? (timestamp + '.') : ''}prismjs.css" as String
        l << prismjs
        l
    }

    @Override
    List<String> getJavascriptFiles() {
        List<String> jsFiles = super.getJavascriptFiles()
        String prismjs = "/javascripts/${timestamp ? (timestamp + '.') : ''}prismjs.js".toString()
        jsFiles << prismjs
        jsFiles
    }
}
