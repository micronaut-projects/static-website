package io.micronaut.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.Navigation
import io.micronaut.IconMenuItem
import io.micronaut.Menu
import io.micronaut.MenuItem
import io.micronaut.TextMenuItem

@CompileStatic
abstract class Page implements HtmlPage {

    String timestamp

    abstract MenuItem menuItem()

    abstract String mainContent()

    abstract String getSlug()

    abstract String getBodyClass()

    abstract String getTitle()

    WebsiteEnvironment environment = WebsiteEnvironment.DEVELOPMENT

    public static final String GOOGLE_ANALYTICS_TRACKING_CODE = 'UA-115754405-1'

    String developmentServer() {
        'http://localhost:8888'
    }

    @Override
    void setTimestamp(String timestamp) {
        this.timestamp = timestamp
    }

    String guidesUrl() {
        switch (environment) {
            case WebsiteEnvironment.DEVELOPMENT:
                return developmentServer()
            case WebsiteEnvironment.STAGING:
                return 'http://guides.sergiodelamo.es'
            case WebsiteEnvironment.PRODUCTION:
                return 'http://guides.micronaut.io'
        }
    }

    String micronautUrl() {
        switch (environment) {
            case WebsiteEnvironment.DEVELOPMENT:
                return developmentServer()
            case WebsiteEnvironment.STAGING:
                return 'http://micronaut.sergiodelamo.es'
            case WebsiteEnvironment.PRODUCTION:
                return 'http://micronaut.io'
        }
    }

    @CompileDynamic
    String renderMenuItems(Menu menu, String id = null) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.nav(id: id ?: '') {
            ul(class: 'container') {
                setOmitEmptyAttributes(true)
                setOmitNullAttributes(true)
                for (MenuItem menuItem : menu.items) {
                    MenuItem activeMenuItem = this.menuItem()

                    if ( menuItem instanceof IconMenuItem ) {
                        IconMenuItem iconMenuItem = menuItem as IconMenuItem
                        li(style: "${iconMenuItem.style ?: ''}", class:"${iconMenuItem.cssClass ?: ''}") {
                            a(href: menuItem.href) {
                                mkp.yieldUnescaped renderImage(iconMenuItem.image, iconMenuItem.alt)
                            }
                        }
                    } else if ( menuItem instanceof TextMenuItem ) {
                        TextMenuItem textMenuItem = menuItem as TextMenuItem
                        if ( activeMenuItem && activeMenuItem?.href?.endsWith(menuItem?.href) ) {

                            li(style: "${textMenuItem.style ?: ''}", class: "active align-right ${textMenuItem.cssClass ?: ''}") {
                                if ( textMenuItem.intro ) {
                                    mkp.yield textMenuItem.intro
                                }
                                a href: textMenuItem.href, textMenuItem.title
                            }
                        } else {
                            li(style: "${textMenuItem.style ?: ''}", class: "align-right ${textMenuItem.cssClass ?: ''}") {
                                if ( textMenuItem.intro ) {
                                    mkp.yield textMenuItem.intro
                                }
                                a href: textMenuItem.href, textMenuItem.title
                            }
                        }
                    }
                }
            }
        }
        writer.toString()
    }

    String getHtmlHeadTitle() {
        if ( !getTitle() ) {
            return 'Micronaut Framework'
        }
        [getTitle(), 'Micronaut Framework'].join(' | ')
    }

    String getMetaDescription() {
        'A modern, JVM-based, full-stack framework for building modular, easily testable microservice and serverless applications.'
    }

    List<String> getCssFiles() {
        ["stylesheets/${timestamp ? (timestamp + '.') : ''}screen.css" as String]
    }

    List<String> getJavascriptFiles() {
        [
                "javascripts/${timestamp ? (timestamp + '.') : ''}navbar_hide_scroll.js" as String,
                "javascripts/${timestamp ? (timestamp + '.') : ''}navigation.js" as String,
        ]
    }

    String getImageAssetPreffix() {
        'images/'
    }

    Menu mainMenu() {
        Navigation.mainMenu(micronautUrl(), guidesUrl())
    }


    Menu socialMediaMenu() {
        Navigation.socialMediaMenu()
    }

    Menu footerMenu() {
        Navigation.footerMenu(micronautUrl(), guidesUrl())
    }

    String mainLogo() {
        'micronaut_logo.svg'
    }

    String mainLogoAlt() {
        'Micronaut Logo'
    }

    @CompileDynamic
    String renderImage(String image, String alt, String className = '') {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.img class: className ?: '', src: "${getImageAssetPreffix()}${image}", alt: alt
        writer.toString()
    }

    @CompileDynamic
    static String micronaut() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.span {
            mkp.yield('Micronaut')
            //sup 'TM'
        }
        writer.toString()
    }

    @CompileDynamic
    String renderFooter() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)

        html.footer {
            div(class: 'container') {
                div(class: 'mobile') {
                    mkp.yieldUnescaped renderMenuItems(footerMenu(), 'footernav')
                }
                p(id: "blacklogo") {
                    img(src: "${getImageAssetPreffix()}${bodyClass == 'home' ? 'white_' : ''}micronautlogo.svg", alt: "Micronaut Logo")
                }
                div(class: 'twocolumns') {
                    div(class: 'column') {
                        if ( bodyClass == 'home' ) {
                            mkp.yieldUnescaped renderMenuItems(Navigation.whiteSocialMediaMenu(), "socialnetworknav")
                        } else {
                            mkp.yieldUnescaped renderMenuItems(socialMediaMenu(), "socialnetworknav")
                        }

                        p { small '© 2018, Object Computing, Inc. (OCI). All rights reserved' }
                    }
                    div(class: 'column', style: 'margin-top: 10px;') {
                        p {
                            a(title: "Visit OCI Website", href: "https://objectcomputing.com") {
                                img(src: "${getImageAssetPreffix()}oci_logo${bodyClass == 'home' ? '_white' : ''}.svg", alt: "Object Computing")
                            }
                        }
                    }
                }
            }
        }
        writer.toString()
    }

    boolean doNotIndex() {
        false
    }

    String title() {
        'Micronaut: A Modern Microservice Framework for the JVM'
    }

    String summary() {
        'A modern, JVM-based, full-stack framework for building modular, easily testable microservice and serverless applications.'
    }

    @CompileDynamic
    String head() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.head {
            if ( doNotIndex() ) {
                meta name: 'robots', content: 'noindex,nofollow'
            }
            title getHtmlHeadTitle()
            if ( getMetaDescription() ) {
                meta name: 'description', content: getMetaDescription()
            }
            meta charset: 'utf-8'
            link rel: 'icon', href: "${micronautUrl()}/${getImageAssetPreffix()}favicon.ico"
            meta name: 'viewport', content: 'width=device-width, initial-scale=1, shrink-to-fit=no'
            meta name: 'twitter:card', content: 'summary_large_image'
            meta name: "twitter:site", content: '@micronautfw'
            meta name: "twitter:creator", content: '@micronautfw'
            meta property: 'og:image', content: "${micronautUrl()}/${getImageAssetPreffix()}Micronaut_OG_Logo.png"
            meta property: 'og:image:width', content: '600'
            meta property: 'og:image:height', content: '300'
            meta property: 'og:url', content: 'http://micronaut.io'
            meta property: 'og:title', content: title()
            meta property: 'og:description', content: summary()
            meta property: 'og:type', content: 'website'
            link rel: 'mask-icon', href: "${micronautUrl()}/${getImageAssetPreffix()}website_icon.svg"

            link rel: 'apple-touch-icon-precomposed', sizes: '57x57', href: "${micronautUrl()}/${getImageAssetPreffix()}apple-touch-icon-57x57.png"
            link rel: 'pple-touch-icon-precomposed', sizes: '114x114', href: "${micronautUrl()}/${getImageAssetPreffix()}apple-touch-icon-114x114.png"
            link rel: 'apple-touch-icon-precomposed', sizes: '72x72', href: "${micronautUrl()}/${getImageAssetPreffix()}apple-touch-icon-72x72.png"
            link rel: 'pple-touch-icon-precomposed', sizes: '144x144', href: "${micronautUrl()}/${getImageAssetPreffix()}apple-touch-icon-144x144.png"
            link rel: 'apple-touch-icon-precomposed', sizes: '60x60', href: "${micronautUrl()}/${getImageAssetPreffix()}apple-touch-icon-60x60.png"
            link rel: 'pple-touch-icon-precomposed', sizes: '120x120', href: "${micronautUrl()}/${getImageAssetPreffix()}apple-touch-icon-120x120.png"
            link rel: 'apple-touch-icon-precomposed', sizes: '76x76', href: "${micronautUrl()}/${getImageAssetPreffix()}apple-touch-icon-76x76.png"
            link rel: 'pple-touch-icon-precomposed', sizes: '152x152', href: "${micronautUrl()}/${getImageAssetPreffix()}apple-touch-icon-152x152.png"
            link rel: 'icon" type="image/png', href: "${micronautUrl()}/${getImageAssetPreffix()}/favicon-196x196.png", sizes: '196x196'
            link rel: 'con" type="image/png', href: "${micronautUrl()}/${getImageAssetPreffix()}/favicon-96x96.png", sizes: '96x96'
            link rel: 'icon" type="image/png', href: "${micronautUrl()}/${getImageAssetPreffix()}/favicon-32x32.png", sizes: '32x32'
            link rel: 'con" type="image/png', href: "${micronautUrl()}/${getImageAssetPreffix()}/favicon-16x16.png", sizes: '16x16'
            link rel: 'icon" type="image/png', href: "${micronautUrl()}/${getImageAssetPreffix()}/favicon-128.png", sizes: '128x128'
            meta name: 'application-name', content: '&nbsp;'
            meta name: 'msapplication-TileColor', content: '#FFFFFF'
            meta name: 'msapplication-TileImage', content: "${micronautUrl()}/${getImageAssetPreffix()}/mstile-144x144.png"
            meta name: 'msapplication-square70x70logo', content: "${micronautUrl()}/${getImageAssetPreffix()}/mstile-70x70.png"
            meta name: 'msapplication-square150x150logo', content: "${micronautUrl()}/${getImageAssetPreffix()}/mstile-150x150.png"
            meta name: 'msapplication-wide310x150logo', content: "${micronautUrl()}/${getImageAssetPreffix()}/mstile-310x150.png"
            meta name: 'msapplication-square310x310logo', content: "${micronautUrl()}/${getImageAssetPreffix()}/mstile-310x310.png"
            if ( getCssFiles() ) {
                for ( String css : getCssFiles() ) {
                    link rel: 'stylesheet', href: css
                }
            }
            if ( getJavascriptFiles() ) {
                for ( String javascriptSrc : getJavascriptFiles() ) {
                    script src: javascriptSrc, ''
                }
            }
        }
        writer.toString()
    }

    @CompileDynamic
    String html() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.html {
            setOmitEmptyAttributes(true)
            setOmitNullAttributes(true)
            mkp.yieldUnescaped(head())
            body(class: getBodyClass()) {
                div(class: 'main') {
                    mkp.yieldUnescaped renderMenuItems(mainMenu(), 'navbar')
                }
                mkp.yieldUnescaped mainContent()
                mkp.yieldUnescaped('<div id="footercircle" class="full whitecircleborderseparator"></div>')
                mkp.yieldUnescaped renderFooter()
                mkp.yieldUnescaped scriptAtClosingBody()

            }

        }
        writer.toString()
    }

    @CompileDynamic
    String scriptAtClosingBody() {
        return """
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=${GOOGLE_ANALYTICS_TRACKING_CODE}"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());
  gtag('config', '${GOOGLE_ANALYTICS_TRACKING_CODE}');
</script>
<script type="text/javascript">
    adroll_adv_id = "J4NA4XCFDNFHDGNFUAVMQD";
    adroll_pix_id = "J4XYMNCOQNBCXJJ4GP744N";

    (function () {
        var _onload = function(){
            if (document.readyState && !/loaded|complete/.test(document.readyState)){setTimeout(_onload, 10);return}
            if (!window.__adroll_loaded){__adroll_loaded=true;setTimeout(_onload, 50);return}
            var scr = document.createElement("script");
            var host = (("https:" == document.location.protocol) ? "https://s.adroll.com" : "http://a.adroll.com");
            scr.setAttribute('async', 'true');
            scr.type = "text/javascript";
            scr.src = host + "/j/roundtrip.js";
            ((document.getElementsByTagName('head') || [null])[0] ||
                document.getElementsByTagName('script')[0].parentNode).appendChild(scr);
        };
        if (window.addEventListener) {window.addEventListener('load', _onload, false);}
        else {window.attachEvent('onload', _onload)}
    }());
</script>
"""
    }

    static String columnsClass(List l) {
        switch ( l.size() ) {
            case 3:
                return 'threecolumns'
            default:
                return 'twocolumns'
        }
    }
}
