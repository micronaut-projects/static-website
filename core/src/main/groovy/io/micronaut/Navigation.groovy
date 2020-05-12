package io.micronaut

import groovy.transform.CompileStatic
import io.micronaut.IconMenuItem
import io.micronaut.Menu
import io.micronaut.MenuItem
import io.micronaut.TextMenuItem

@CompileStatic
class Navigation {

    public static final String OCI_WEBSITE = 'https://objectcomputing.com'

    static Menu mainMenu(String url = null, String guidesUrl = null) {
        new Menu(items: [
                footerNavTextItem(),
                githubMenuItem(),
                desktopSupportMenuItem(url),
                mobileSupportMenuItem(url),
                questionsMenuItem(url),
                desktopEventsItem(url),
                mobileEventsItem(url),
                desktopBlogMenuItem(url),
                mobileBlogMenuItem(url),
                webinarMenuItem(),
                trainingMenuItem(),
                guidesMenuItem(guidesUrl),
                downloadMenuItem(url),
                documentationMenuItem(url),
                docsMenuItem(url),
                launchMenuItem(url),
                desktopHomeMenuItem(url),
                mobileHomeMenuItem(url)
        ] as List<MenuItem>)
    }

    static Menu footerMenu(String url = null, String guidesUrl = null) {
        new Menu(items: [
                launchMenuItem(url),
                docsMenuItem(url),
                downloadMenuItem(url),
                guidesMenuItem(guidesUrl),
                webinarMenuItem(),
                trainingMenuItem(),
                mobileEventsItem(url),
                mobileSupportMenuItem(url, 'Support'),
                questionsMenuItem(url),
                mobileBlogMenuItem(url),
        ] as List<MenuItem>)
    }

    static Menu socialMediaMenu() {
        new Menu(items: [
                new IconMenuItem(image: 'gitter.svg', href: 'https://gitter.im/micronautfw/', alt: 'Gitter Icon'),
                new IconMenuItem(image: 'youtube.svg', href: 'https://www.youtube.com/watch?v=56j_f3OCg6E&list=PLI74De5M9T71c2dUQYy0nCdKUgZE2rrVD', alt: 'Youtube Icon'),
//                new IconMenuItem(image: 'linkedin.svg', href: 'https://www.linkedin.com/showcase/micronaut/', alt: 'LinkedIn Icon'),
                new IconMenuItem(image: 'github.svg', href: 'https://github.com/micronaut-projects/', alt: 'Github Icon'),
                new IconMenuItem(image: 'twitter.svg', href: 'https://twitter.com/micronautfw', alt: 'Twitter Icon'),
                new IconMenuItem(image: 'mail.svg', href: 'mailto:info@micronaut.io?subject=Micronaut&amp;body=I%20am%20interested%20in%20Micronaut.', alt: 'Mail Icon'),
        ] as List<MenuItem>)
    }

    static Menu whiteSocialMediaMenu() {
        new Menu(items: [
                new IconMenuItem(image: 'white_gitter.svg', href: 'https://gitter.im/micronautfw/', alt: 'Slack Icon'),
                new IconMenuItem(image: 'white_youtube.svg', href: 'https://www.youtube.com/watch?v=56j_f3OCg6E&list=PLI74De5M9T71c2dUQYy0nCdKUgZE2rrVD', alt: 'Youtube Icon'),
//                new IconMenuItem(image: 'white_linkedin.svg', href: 'https://www.linkedin.com/showcase/micronaut/', alt: 'LinkedIn Icon'),
                new IconMenuItem(image: 'white_github.svg', href: 'https://github.com/micronaut-projects/', alt: 'Github Icon'),
                new IconMenuItem(image: 'white_twitter.svg', href: 'https://twitter.com/micronautfw', alt: 'Twitter Icon'),
                new IconMenuItem(image: 'white_mail.svg', href: 'mailto:info@micronaut.io?subject=Micronaut&amp;body=I%20am%20interested%20in%20Micronaut.', alt: 'Mail Icon'),
        ] as List<MenuItem>)
    }

    static MenuItem learningMenuItem(String url = null) {
        menuItemWithHref('learning.html', 'Learning', url)
    }

    static TextMenuItem communityMenuItem(String url = null) {
        menuItemWithHref('community.html', 'Community', url)
    }

    static TextMenuItem profilesMenuItem(String url = null) {
        menuItemWithHref('profiles.html', 'Profiles', url)
    }

    static TextMenuItem booksMenuItem(String url = null) {
        menuItemWithHref('books.html', 'Books', url)
    }

    static TextMenuItem searchMenuItem(String url = null) {
        menuItemWithHref('search.html', 'Search', url)
    }

    static TextMenuItem docsMenuItem(String url = null) {
        menuItemWithHref('documentation.html', 'Docs', url, 'mobile')
    }

    static TextMenuItem documentationMenuItem(String url = null) {
        menuItemWithHref('documentation.html', 'Docs', url, 'desktop')
    }

    static TextMenuItem downloadMenuItem(String url = null) {
        menuItemWithHref('download.html', 'Download', url)
    }

    static TextMenuItem launchMenuItem(String url = null) {
        menuItemWithHref('launch/', 'Launch', url)
    }

    static TextMenuItem guidesMenuItem(String guidesUrl) {
        menuItemWithHref('index.html', 'Guides', guidesUrl)
    }

    static TextMenuItem questionsMenuItem(String url = null) {
        menuItemWithHref('faq.html', 'FAQ', url)
    }

    static TextMenuItem desktopSupportMenuItem(String url = null) {
        menuItemWithHref('support.html', 'Support', url, 'desktop')
    }

    static TextMenuItem mobileSupportMenuItem(String url = null, String title = 'Support') {
        menuItemWithHref('support.html', title, url, 'mobile')
    }


    static String WEBINAR_PATH = '/products/micronaut/resources#on-demand'

    static String TRAINING_PATH = '/services/training/catalog/micronaut-training#catalog'

    static TextMenuItem webinarMenuItem() {
        menuItemWithHref(WEBINAR_PATH, 'Webinars', OCI_WEBSITE)
    }

    static TextMenuItem trainingMenuItem() {
        menuItemWithHref(TRAINING_PATH, 'Training', OCI_WEBSITE)
    }

    static TextMenuItem desktopBlogMenuItem(String url = null) {
        menuItemWithHref('/news/category/micronaut-blog', 'News', OCI_WEBSITE, 'desktop')
    }

    static TextMenuItem mobileBlogMenuItem(String url = null, String title = 'News') {
        menuItemWithHref('/news/category/micronaut-blog', title, OCI_WEBSITE, 'mobile')
    }

    static IconMenuItem mobileHomeMenuItem(String url = null) {
        new IconMenuItem(image: 'micronaut_mini.svg', href: "${url}/index.html", alt: 'Micronaut Icon', cssClass: 'small align-left')
    }
    static IconMenuItem desktopHomeMenuItem(String url = null) {
        new IconMenuItem(image: 'micronaut_mini_copy_tm.svg', href: "${url}/index.html", alt: 'Micronaut Icon', cssClass: 'large align-left')
    }

    static IconMenuItem githubMenuItem() {
        new IconMenuItem(image: 'github.svg', href: 'https://github.com/micronaut-projects/', alt: 'Github', cssClass: 'align-right icon')
    }

    static TextMenuItem mobileEventsItem(String url = null) {
        menuItemWithHref('events.html', 'Events', url, 'mobile')
    }

    static TextMenuItem footerNavTextItem(String url = null) {
        menuItemWithHref('#footercircle', 'Menu', url, 'tiny')
    }

    static TextMenuItem desktopEventsItem(String url = null) {
        menuItemWithHref('events.html', 'Events', url, 'desktop')
    }

    static TextMenuItem menuItemWithHref(String relativePath, String title, String url, String cssClass = null) {
        String href = relativePath
        if (url) {
            href = url
            if(relativePath.startsWith("/")) {
                href += relativePath
            } else {
                href += "/${relativePath}"
            }
        }
        new TextMenuItem(href: href, title: title, cssClass: cssClass)
    }


}
