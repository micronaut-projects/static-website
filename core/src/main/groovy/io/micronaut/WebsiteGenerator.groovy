package io.micronaut

import groovy.transform.CompileStatic
import io.micronaut.pages.HtmlPage

@CompileStatic
class WebsiteGenerator {

    static savePage(HtmlPage page, String filename) {
        String text = page.html()
        String preffix = "build/site/"
        File f = new File("${preffix}${filename}")
        f.createNewFile()
        f.text = text
    }

    static copyAsset(String assetType, String fileName, String timestamp) {
        File f = new File("../main/src/main/resources/assets/${assetType}/${fileName}")
        String text = f.text
        String preffix = "build/site/${assetType}/"
        File output = new File("${preffix}${timestamp}.${fileName}")
        output.createNewFile()
        output.text = text
    }

    static String timestamp() {
        new Date().format("MddHHmmss", TimeZone.getTimeZone('UTC'))
    }

    static void copyAssetsWithTimestamp(String timestamp) {
        WebsiteGenerator.copyAsset('stylesheets', 'screen.css', timestamp)
        WebsiteGenerator.copyAsset('javascripts', 'navigation.js', timestamp)
        WebsiteGenerator.copyAsset('javascripts', 'navbar_hide_scroll.js', timestamp)
        WebsiteGenerator.copyAsset('javascripts', 'search.js', timestamp)
        WebsiteGenerator.copyAsset('stylesheets', 'prismjs.css', timestamp)
        WebsiteGenerator.copyAsset('javascripts', 'prismjs.js', timestamp)
    }
}