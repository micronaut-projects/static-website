package io.micronaut.main

import groovy.transform.CompileStatic
import io.micronaut.GuidesFetcher
import io.micronaut.WebsiteGenerator
import io.micronaut.Guide
import io.micronaut.pages.HtmlPage
import io.micronaut.pages.IFramePage
import io.micronaut.pages.Page
import io.micronaut.pages.SiteMapPage

@CompileStatic
class Main {
    static void main(String[] args) {
        List<Guide> guides = GuidesFetcher.fetchGuides()
        List<HtmlPage> pages = SiteMap.PAGES

        pages << new IFramePage('http://docs.micronaut.io/snapshot/api/', 'api.html', null)

        String timestamp = WebsiteGenerator.timestamp()

        for (HtmlPage page : pages) {
            page.timestamp = timestamp
            WebsiteGenerator.savePage(page, page.slug)
        }

        WebsiteGenerator.copyAssetsWithTimestamp(timestamp)

        String micronautUrl = (pages.get(0) as Page).micronautUrl()

        List<String> urls = pages.collect { HtmlPage page -> "${micronautUrl}/${page.slug}" as String }
        SiteMapPage siteMapPage = new SiteMapPage(urls)
        WebsiteGenerator.savePage(siteMapPage, siteMapPage.slug)
    }
}