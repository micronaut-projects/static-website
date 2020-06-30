package io.micronaut.gradle

import groovy.transform.CompileStatic
import groovy.transform.Internal
import io.micronaut.ContentAndMetadata
import io.micronaut.Page
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import static groovy.io.FileType.FILES

import javax.annotation.Nonnull
import javax.validation.constraints.NotNull
import java.text.SimpleDateFormat

@CompileStatic
class RenderSiteTask extends DefaultTask {

    static final SimpleDateFormat MMM_D_YYYY = new SimpleDateFormat("MMM d, yyyy")

    static final String COLON = ":"
    static final String SEPARATOR = "---"
    public static final String DIST = "dist"

    @InputDirectory
    final Property<File> pages = project.objects.property(File)

    @Input
    final Property<File> template = project.objects.property(File)

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

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @Internal
    @Input
    final Provider<File> document = template.map { new File(it.absolutePath + '/Contents/Resources/document.html') }

    @TaskAction
    void renderSite() {
        File template = document.get()
        final String templateText = template.text
        File o = output.get()
        Map<String, String> m = siteMeta(title.get(), about.get(), url.get(), keywords.get() as List<String>, robots.get())
        List<Page> listOfPages = parsePages(pages.get())
        listOfPages.addAll(parsePages(new File(o.absolutePath + "/" + DocumentationTask.TEMP)))
        File dist = new File(o.absolutePath + "/" + DIST)
        renderPages(m, listOfPages, dist, templateText)
    }

    static Map<String, String> siteMeta(String title,
                                        String about,
                                        String url,
                                        List<String> keywords,
                                        String robots) {
        [
                title: title,
                description: about,
                url: url,
                keywords: keywords.join(','),
                robots: robots,
                gitter: 'gitter.svg',
                youtube: 'youtube.svg',
                github: 'github.svg',
                twitter: 'twitter.svg',
                mail: 'mail.svg',
                micronautlogo: 'micronautlogo.svg',
                ocilogo: 'oci_logo.svg'
        ] as Map<String, String>
    }

    static void renderPages(Map<String, String> sitemeta, List<Page> listOfPages, File outputDir, final String templateText) {
        for (Page page : listOfPages) {
            Map<String, String> resolvedMetadata = processMetadata(sitemeta + page.metadata)
            String html = renderHtmlWithTemplateContent(page.content, resolvedMetadata, templateText)
            html = highlightMenu(html, sitemeta, page.path)
            if (page.body) {
                html = html.replace("<body>", "<body class='${page.body}'>")
            }
            saveHtmlToPath(outputDir, html, page.path)
        }
    }

    static void saveHtmlToPath(File outputDir, String html, String filepath) {
        File pageOutput = new File(outputDir.absolutePath)
        pageOutput.mkdir()
        String[] paths = filepath.split('/')
        for (String path : paths) {
            if (path.endsWith(".html")) {
                pageOutput = new File(pageOutput.getAbsolutePath() + "/" + path)
            } else if (path.trim().isEmpty()) {
                continue
            } else {
                pageOutput = new File(pageOutput.getAbsolutePath() + "/" + path)
                pageOutput.mkdir()
            }
        }
        pageOutput.createNewFile()
        pageOutput.text = html
    }

    static Map<String, String> processMetadata(Map<String, String> sitemeta) {
        Map<String, String> resolvedMetadata = sitemeta
        if (resolvedMetadata.containsKey("CSS")) {
            resolvedMetadata.put("CSS", "<link rel='stylesheet' href='" + resolvedMetadata['CSS'] + "'/>")
        } else {
            resolvedMetadata.put("CSS", "")
        }

        if (resolvedMetadata.containsKey("JAVASCRIPT")) {
            resolvedMetadata.put("JAVASCRIPT", "<script src='" + resolvedMetadata['JAVASCRIPT'] + "'></script>")
        } else {
            resolvedMetadata.put("JAVASCRIPT", "")
        }

        if (!resolvedMetadata.containsKey("HTML header")) {
            resolvedMetadata.put("HTML header", "")
        }
        if (!resolvedMetadata.containsKey("keywords")) {
            resolvedMetadata.put('keywords', "")
        }
        if (!resolvedMetadata.containsKey("description")) {
            resolvedMetadata.put('description', "")
        }
        if (!resolvedMetadata.containsKey("date")) {
            resolvedMetadata.put('date', MMM_D_YYYY.format(new Date()))
        }
        if (!resolvedMetadata.containsKey("robots")) {
            resolvedMetadata.put('robots', "all")
        }
        resolvedMetadata
    }

    static String highlightMenu(String html, Map<String, String> sitemeta, String path) {
        html.replaceAll("<li><a href='" + sitemeta['url'] + path, "<li class='active'><a href='" + sitemeta['url'] + path)
    }

    static List<Page> parsePages(File pages) {
        List<Page> listOfPages = []
        pages.eachFileRecurse(FILES) { file ->
            if (file.path.endsWith(".html")) {
                ContentAndMetadata contentAndMetadata = parseFile(file)
                String filename = file.absolutePath.replace(pages.absolutePath, "")
                listOfPages << new Page(filename: filename, content: contentAndMetadata.content, metadata: contentAndMetadata.metadata)
            }
        }
        listOfPages
    }

    static ContentAndMetadata parseFile(File file) {
        String line = null
        List<String> lines = []
        Map<String, String> metadata = [:]
        boolean metadataProcessed = false
        file.withReader { reader ->
            while ((line = reader.readLine()) != null) {
                if (line.contains(SEPARATOR)) {
                    metadataProcessed = true
                    continue
                }
                if (!metadataProcessed && line.contains(COLON)) {
                    String metadataKey = line.substring(0, line.indexOf(COLON as String)).trim()
                    String metadataValue = line.substring(line.indexOf(COLON as String) + COLON.length()).trim()
                    metadata[metadataKey] = metadataValue
                }
                line = replaceLineWithMetadata(line, metadata)
                if (metadataProcessed) {
                    lines << line
                }
            }
        }

        !metadataProcessed || lines.isEmpty() ? new ContentAndMetadata(metadata: [:] as Map<String, String>, content: file.text) :
                new ContentAndMetadata(metadata: metadata, content: lines.join("\n"))
    }

    @Nonnull
    static String renderHtmlWithTemplateContent(@Nonnull @NotNull String html,
                                         @Nonnull @NotNull Map<String, String> meta,
                                         @NotNull @Nonnull final String templateText) {
        String outputHtml = templateText
        String result = outputHtml.replace(' data-document>', ">" + html)
        result = replaceLineWithMetadata(result, meta)
        result
    }

    static String replaceLineWithMetadata(String line, Map<String, String> metadata) {
        for (String metadataKey : metadata.keySet()) {
            if (line.contains("[%${metadataKey}]".toString())) {
                String value = metadata[metadataKey]
                if ("[%${metadataKey}]".toString() == '[%author]') {
                    List<String> authors = value.split(",") as List<String>
                    value = '<span class="author">By ' + authors.join("<br/>") + '</span>'
                    line = line.replaceAll("\\[%${metadataKey}\\]".toString(), value)

                } else if ("[%${metadataKey}]".toString() == '[%date]') {
                    if (line.contains('<meta')) {
                        line = line.replaceAll("\\[%${metadataKey}\\]".toString(), value)
                    } else {
                        value = '<span class="date">' + value + '</span>'
                        line = line.replaceAll("\\[%${metadataKey}\\]".toString(), value)
                    }
                } else {
                    line = line.replaceAll("\\[%${metadataKey}\\]".toString(), value)
                }
            }
        }
        line.replaceAll('\\$', '&#36;')
    }
}
