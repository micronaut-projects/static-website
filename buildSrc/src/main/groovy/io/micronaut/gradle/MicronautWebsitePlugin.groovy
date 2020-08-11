package io.micronaut.gradle

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin

@CompileStatic
class MicronautWebsitePlugin implements Plugin<Project> {

    public static final String EXTENSION_NAME = "micronaut"
    public static final String TASK_GEN_DOCS = "genDocs"
    public static final String TASK_GEN_DOWNLOAD = "genDownload"
    public static final String TASK_GEN_GUIDES = "genGuides"
    public static final String TASK_GEN_FAQ = "genFaq"
    public static final String TASK_GEN_MN_LOGOS = "genMicronautLogos"
    public static final String TASK_CLEAN_DOCS = "cleanDocs"
    public static final String TASK_CLEAN_FAQ = "cleanFaq"
    public static final String TASK_CLEAN_MN_LOGOS = "cleanMicronautLogos"
    public static final String TASK_CLEAN_EVENTS = "cleanEvents"
    public static final String TASK_CLEAN_GUIDES = "cleanGuides"
    public static final String TASK_CLEAN_DOWNLOAD = "cleanDownload"
    public static final String TASK_BUILD = "build"
    public static final String TASK_GEN_SITE = "renderSite"
    public static final String TASK_GEN_SITEMAP = "genSitemap"
    public static final String CLEAN = "clean"
    public static final String TASK_GEN_EVENTS = "genEvents"
    public static final String TASK_COPY_ASSETS = "copyAssets"
    public static final String BUILD_GUIDES = "buildGuides"
    public static final String GROUP_MICRONAUT = 'micronaut'
    public static final String TASK_RENDER_BLOG = 'renderBlog'


    @Override
    void apply(Project project) {
        project.getPlugins().apply(BasePlugin.class)
        project.extensions.create(EXTENSION_NAME, SiteExtension)

        project.tasks.register(TASK_GEN_DOCS, DocumentationTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("modules", siteExtension.modules)
                task.setProperty("releases", siteExtension.releases)
                task.setProperty("output", siteExtension.output)
                task.setProperty("url", siteExtension.url)
            }
            task.setDescription('Generates documentation HTML page - build/temp/documentation.html')
            task.setGroup(GROUP_MICRONAUT)
        })
        project.tasks.register(TASK_GEN_SITEMAP, SitemapTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("output", siteExtension.output)
                task.setProperty("url", siteExtension.url)
            }
            task.setGroup(GROUP_MICRONAUT)
            task.setDescription('Generates build/dist/sitemap.xml with every page in the site')
        })
        project.tasks.register(TASK_RENDER_BLOG, BlogTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("url", siteExtension.url)
                task.setProperty("title", siteExtension.title)
                task.setProperty("about", siteExtension.description)
                task.setProperty("keywords", siteExtension.keywords)
                task.setProperty("robots", siteExtension.robots)
                task.setProperty("document", siteExtension.template)
                task.setProperty("output", siteExtension.output)
                task.setProperty("posts", siteExtension.posts)
                task.setProperty("assets", siteExtension.assets)
            }
            task.setGroup(GROUP_MICRONAUT)
            task.description = 'Renders Markdown posts (posts/*.md) into HTML pages (dist/blog/*.html). It generates tag pages. Generates RSS feed. Posts with future dates are not generated.'
        })
        project.tasks.register(TASK_GEN_FAQ, QuestionsTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("questions", siteExtension.questions)
                task.setProperty("output", siteExtension.output)
            }
            task.setDescription("Generates FAQ HTML - build/temp/faq.html ")
            task.setGroup(GROUP_MICRONAUT)
        })
        project.tasks.register(TASK_GEN_MN_LOGOS, LogosTask, { task ->
            task.setEnabled(false)
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("logos", siteExtension.micronautLogos)
                task.setProperty("output", siteExtension.output)
            }
            task.setDescription("Generates page with Micronaut Logos - build/temp/micronaut-logos.html ")
            task.setGroup(GROUP_MICRONAUT)
        })
        project.tasks.register(TASK_GEN_EVENTS, EventsTask, { task ->
            task.setOnlyIf {
                System.getenv("AIRTABLE_API_KEY") != null &&
                System.getenv("AIRTABLE_BASE_ID") != null
            }
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("url", siteExtension.url)
                task.setProperty("output", siteExtension.output)
            }
            task.setDescription('Generates events HTML page - build/temp/events.html')
            task.setGroup(GROUP_MICRONAUT)
        })
        project.tasks.register(TASK_GEN_GUIDES, GuidesTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("url", siteExtension.url)
                task.setProperty("title", siteExtension.title)
                task.setProperty("about", siteExtension.description)
                task.setProperty("keywords", siteExtension.keywords)
                task.setProperty("robots", siteExtension.robots)
                task.setProperty("document", siteExtension.template)
                task.setProperty("output", siteExtension.output)
            }
            task.setDescription('Generates guides home, tags and categories HTML pages - build/temp/index.html')
            task.setGroup(GROUP_MICRONAUT)
        })
        project.tasks.register(TASK_GEN_DOWNLOAD, DownloadTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("releases", siteExtension.releases)
                task.setProperty("url", siteExtension.url)
                task.setProperty("output", siteExtension.output)
            }
            task.setDescription('Generates download HTML page - build/temp/download.html')
            task.setGroup(GROUP_MICRONAUT)
        })
        project.tasks.register(TASK_COPY_ASSETS, CopyAssetsTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("output", siteExtension.output)
                task.setProperty("assets", siteExtension.assets)
            }
            task.setDescription('Copies css, js, fonts and images from the assets folder to the dist folder')
            task.setGroup(GROUP_MICRONAUT)
        })

        project.tasks.register(BUILD_GUIDES, BuildGuidesTask, { task ->
            task.dependsOn(TASK_COPY_ASSETS)
            task.dependsOn(TASK_GEN_GUIDES)
            task.setGroup(GROUP_MICRONAUT)
            task.finalizedBy(TASK_GEN_SITEMAP)
            task.setDescription('Build guides website - generates guides pages, copies assets and generates a sitemap')
        })
        project.tasks.register(TASK_GEN_SITE, RenderSiteTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("url", siteExtension.url)
                task.setProperty("title", siteExtension.title)
                task.setProperty("about", siteExtension.description)
                task.setProperty("keywords", siteExtension.keywords)
                task.setProperty("robots", siteExtension.robots)
                task.setProperty("document", siteExtension.template)
                task.setProperty("output", siteExtension.output)
                task.setProperty("pages", siteExtension.pages)
            }
            task.setGroup(GROUP_MICRONAUT)
            task.dependsOn(TASK_COPY_ASSETS)
            task.dependsOn(TASK_GEN_DOCS)
            task.dependsOn(TASK_GEN_FAQ)
            task.dependsOn(TASK_GEN_MN_LOGOS)
            task.dependsOn(TASK_GEN_DOWNLOAD)
            task.dependsOn(TASK_GEN_EVENTS)
            task.finalizedBy(TASK_RENDER_BLOG)
            task.finalizedBy(TASK_GEN_SITEMAP)
            task.setDescription('Build Micronaut website - generates pages with HTML entries in pages and build/temp, renders blog and RSS feed, copies assets and generates a sitemap')

        })
        project.tasks.register(TASK_CLEAN_GUIDES, { task ->
            task.setDescription('Deletes temp Guides page: build/temp/index.html ')
            task.setGroup(GROUP_MICRONAUT)
            task.doLast {
                Object extension = project.getExtensions().findByName(EXTENSION_NAME)
                if (extension instanceof SiteExtension) {
                    SiteExtension siteExtension = ((SiteExtension) extension)
                    File f = new File(siteExtension.pages.get().getAbsolutePath() + "/" + GuidesTask.PAGE_NAME_GUIDES )
                    f.delete()
                    f = new File(siteExtension.pages.get().getAbsolutePath() + "/" + GuidesTask.CATEGORIES)
                    f.deleteDir()
                    f = new File(siteExtension.pages.get().getAbsolutePath() + "/" + GuidesTask.TAGS)
                    f.deleteDir()
                }
            }
        })
        project.tasks.register(TASK_CLEAN_DOCS, { task ->
            task.setGroup(GROUP_MICRONAUT)
            task.setDescription("Deletes documentation temp page - build/temp/documentation.html")
            task.doLast {
                Object extension = project.getExtensions().findByName(EXTENSION_NAME)
                if (extension instanceof SiteExtension) {
                    SiteExtension siteExtension = ((SiteExtension) extension)
                    File f = new File(siteExtension.pages.get().getAbsolutePath() + "/" + DocumentationTask.PAGE_NAME_DOCS)
                    f.delete()
                }
            }
        })
        project.tasks.register(TASK_CLEAN_FAQ, { task ->
            task.setGroup(GROUP_MICRONAUT)
            task.setDescription("Deletes faq temp page - build/temp/faq.html")
            task.doLast {
                Object extension = project.getExtensions().findByName(EXTENSION_NAME)
                if (extension instanceof SiteExtension) {
                    SiteExtension siteExtension = ((SiteExtension) extension)
                    File f = new File(siteExtension.pages.get().getAbsolutePath() + "/" + QuestionsTask.PAGE_NAME_QUESTIONS)
                    f.delete()
                }
            }
        })
        project.tasks.register(TASK_CLEAN_MN_LOGOS, { task ->
            task.setGroup(GROUP_MICRONAUT)
            task.setDescription("Deletes mn logos page - build/temp/mn-logos.html")
            task.doLast {
                Object extension = project.getExtensions().findByName(EXTENSION_NAME)
                if (extension instanceof SiteExtension) {
                    SiteExtension siteExtension = ((SiteExtension) extension)
                    File f = new File(siteExtension.pages.get().getAbsolutePath() + "/" + LogosTask.PAGE_NAME_MN_LOGOS)
                    f.delete()
                }
            }
        })
        project.tasks.register(TASK_CLEAN_EVENTS, { task ->
            task.setGroup(GROUP_MICRONAUT)
            task.setDescription("Deletes events temp page - build/temp/events.html")
            task.doLast {
                Object extension = project.getExtensions().findByName(EXTENSION_NAME)
                if (extension instanceof SiteExtension) {
                    SiteExtension siteExtension = ((SiteExtension) extension)
                    File f = new File(siteExtension.pages.get().getAbsolutePath() + "/" + EventsTask.PAGE_NAME_EVENTS)
                    f.delete()
                }
            }
        })
        project.tasks.register(TASK_CLEAN_DOWNLOAD, { task ->
            task.setGroup(GROUP_MICRONAUT)
            task.setDescription("Deletes download temp page - build/temp/download.html")
            task.doLast {
                Object extension = project.getExtensions().findByName(EXTENSION_NAME)
                if (extension instanceof SiteExtension) {
                    SiteExtension siteExtension = ((SiteExtension) extension)
                    File f = new File(siteExtension.pages.get().getAbsolutePath() + "/" + DownloadTask.PAGE_NAME_DOWNLOAD)
                    f.delete()
                }
            }
        })
        project.tasks.named(TASK_BUILD).configure(new Action<Task>() {
            @Override
            void execute(Task task) {
                task.dependsOn(TASK_GEN_SITE)
            }
        })
        project.tasks.named(CLEAN).configure(new Action<Task>() {
            @Override
            void execute(Task task) {
                task.dependsOn(TASK_CLEAN_GUIDES)
                task.dependsOn(TASK_CLEAN_FAQ)
                task.dependsOn(TASK_CLEAN_DOCS)
                task.dependsOn(TASK_CLEAN_DOWNLOAD)
                task.dependsOn(TASK_CLEAN_EVENTS)
                task.dependsOn(TASK_CLEAN_MN_LOGOS)
            }
        })
    }
}
