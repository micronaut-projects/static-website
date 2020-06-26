package io.micronaut.gradle

import groovy.transform.CompileStatic
import groovy.transform.Internal
import io.micronaut.ContentAndMetadata
import io.micronaut.Page
import io.micronaut.guides.Category
import io.micronaut.guides.Guide
import io.micronaut.guides.GuidesFetcher
import io.micronaut.guides.GuidesPage
import io.micronaut.guides.TagUtils
import io.micronaut.tags.Tag
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@CompileStatic
class GuidesTask extends DefaultTask {

    static final String PAGE_NAME_GUIDES = "guides.html"
    public static final String CATEGORIES = "categories"
    public static final String TAGS = "tags"

    @Input
    final Property<File> template = project.objects.property(File)

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @OutputDirectory
    final Property<File> pages = project.objects.property(File)

    @Input
    final Property<String> title = project.objects.property(String)

    @Input
    final Property<String> about = project.objects.property(String)

    @Input
    final Property<String> url = project.objects.property(String)

    @Input
    final ListProperty<String> keywords = project.objects.listProperty(String)

    @Input
    final Property<String> robots = project.objects.property(String)

    @Internal
    @Input
    final Provider<File> document = template.map { new File(it.absolutePath + '/Contents/Resources/document.html') }

    @TaskAction
    void renderGuides() {
        File pagesDir = pages.get()
        generateGuidesPages(pagesDir, url.get())
        File template = document.get()
        final String templateText = template.text
        File o = output.get()
        Map<String, String> m = RenderSiteTask.siteMeta(title.get(), about.get(), url.get(), keywords.get() as List<String>, robots.get())

        File f = new File(pagesDir.absolutePath + "/" + GuidesTask.PAGE_NAME_GUIDES)
        Page page = pageWithFile(f)
        page.filename = 'index.html'
        RenderSiteTask.renderPages(m, [page], o, templateText)

        List<Page> listOfPages = parseCategoryPages(pagesDir)
        File categoriesOutput = new File(o.absolutePath + "/" + CATEGORIES)
        categoriesOutput.mkdir()
        RenderSiteTask.renderPages(m, listOfPages, categoriesOutput, templateText)

        listOfPages = parseTagsPages(pagesDir)
        File tagOutput = new File(o.absolutePath + "/" + TAGS)
        tagOutput.mkdir()
        RenderSiteTask.renderPages(m, listOfPages, tagOutput, templateText)
    }

    static List<Page> parseCategoryPages(File pages) {
        List<Page> listOfPages = []
        new File(pages.absolutePath + "/" + CATEGORIES).eachFile { categoryFile ->
            listOfPages << pageWithFile(categoryFile)
        }
        listOfPages
    }

    static List<Page> parseTagsPages(File pages) {
        List<Page> listOfPages = []
        new File(pages.absolutePath + "/" + TAGS).eachFile { tagFile ->
            listOfPages << pageWithFile(tagFile)
        }
        listOfPages
    }

    static Page pageWithFile(File f) {
        ContentAndMetadata contentAndMetadata = RenderSiteTask.parseFile(f)
        new Page(filename: f.name, content: contentAndMetadata.content, metadata: contentAndMetadata.metadata)
    }

    static void generateGuidesPages(File pages, String url) {
        List<Guide> guides = GuidesFetcher.fetchGuides()
        Set<Tag> tags = TagUtils.populateTags(guides)

        File pageOutput = new File(pages.getAbsolutePath() + "/" + PAGE_NAME_GUIDES)
        pageOutput.createNewFile()
        pageOutput.text = "body: guides\nJAVASCRIPT: ${url}/javascripts/search.js\n---\n" +
                GuidesPage.mainContent(url, guides, tags)

        File tagsDir = new File(pages.getAbsolutePath() + "/" + TAGS)
        tagsDir.mkdir()
        for (Tag tag : tags) {
            String slug = "${tag.slug.toLowerCase()}.html"
            pageOutput = new File(tagsDir.getAbsolutePath() + "/" + slug)
            pageOutput.createNewFile()
            pageOutput.text = "body: guides---\n" +
                    GuidesPage.mainContent(url, guides, tags, null, tag)
        }
        File categoriesDir = new File(pages.getAbsolutePath() + "/" + CATEGORIES)
        categoriesDir.mkdir()
        for (Category category : GuidesPage.categories().values() ) {
            String slug = "${category.slug.toLowerCase()}.html"
            pageOutput = new File(categoriesDir.getAbsolutePath() + "/" + slug)
            pageOutput.createNewFile()
            pageOutput.text = "body: guides---\n" +
                    GuidesPage.mainContent(url, guides, tags, category, null)
        }
    }
}
