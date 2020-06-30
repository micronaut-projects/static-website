package io.micronaut.gradle

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.Internal
import groovy.xml.MarkupBuilder
import io.micronaut.ContentAndMetadata
import io.micronaut.HtmlPost
import io.micronaut.MarkdownPost
import io.micronaut.MarkdownUtil
import io.micronaut.PostMetadata
import io.micronaut.PostMetadataAdapter
import io.micronaut.events.EventsPage
import io.micronaut.rss.DefaultRssFeedRenderer
import io.micronaut.rss.RssChannel
import io.micronaut.rss.RssFeedRenderer
import io.micronaut.rss.RssItem
import io.micronaut.tags.Tag
import io.micronaut.tags.TagCloud
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.file.CopySpec
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.annotation.Nonnull
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@CompileStatic
class BlogTask extends DefaultTask {

    static final SimpleDateFormat MMM_D_YYYY = new SimpleDateFormat("MMM d, yyyy")
    public static final String RSS_FILE = 'rss.xml'
    public static final String IMAGES = 'images'
    final static String HASHTAG_SPAN = "<span class=\"hashtag\">#"
    final static String SPAN_CLOSE = "</span>"
    public static final int MAX_RELATED_POSTS = 2
    public static final String BLOG = 'blog'
    public static final String TAG = 'tag'
    public static final String INDEX = 'index.html'
    public static final String YOUTUBE_WATCH = 'https://www.youtube.com/watch?v='

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

    @InputDirectory
    final Property<File> posts = project.objects.property(File)

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @Internal
    @Input
    final Provider<File> document = template.map { new File(it.absolutePath + '/Contents/Resources/document.html') }

    @InputDirectory
    final Property<File> assets = project.objects.property(File)

    File dist() {
        new File(output.get().absolutePath + "/" + RenderSiteTask.DIST)
    }

    @TaskAction
    void renderBlog() {
        File template = document.get()
        final String templateText = template.text
        File o = dist()
        Map<String, String> m = RenderSiteTask.siteMeta(title.get(), about.get(), url.get(), keywords.get() as List<String>, robots.get())
        copyBackgroundImages()
        List<MarkdownPost> listOfPosts = parsePosts(posts.get())
        listOfPosts = listOfPosts.sort { a, b ->
            MMM_D_YYYY.parse(a.date).after(MMM_D_YYYY.parse(b.date)) ? -1 : 1
        }
        List<HtmlPost> htmlPosts = processPosts(m, listOfPosts)
        File blog = new File(o.absolutePath + '/' + BLOG)
        blog.mkdir()
        renderPosts(m, htmlPosts, blog, templateText)
        copyBlogImages()
    }

    void copyBlogImages() {
        File images = new File(posts.get().absolutePath)
        File outputBlogImages = new File(dist().absolutePath + '/' + BLOG)
        outputBlogImages.mkdir()
        project.copy(new Action<CopySpec>() {
            @Override
            void execute(CopySpec copySpec) {
                copySpec.from(images)
                copySpec.into(outputBlogImages)
                copySpec.include(CopyAssetsTask.IMAGE_EXTENSIONS)
            }
        })
    }

    void copyBackgroundImages() {
        File images = new File(assets.get().absolutePath + "/" + "bgimages")
        File outputImages = new File(dist().absolutePath + '/images')
        outputImages.mkdir()
        project.copy(new Action<CopySpec>() {
            @Override
            void execute(CopySpec copySpec) {
                copySpec.from(images)
                copySpec.into(outputImages)
                copySpec.include(CopyAssetsTask.IMAGE_EXTENSIONS)
            }
        })
    }

    static RssItem rssItemWithPage(String title, Date pubDate, String link, String guid, String html) {
        RssItem.builder()
                .title(title)
                .pubDate(ZonedDateTime.of(Instant.ofEpochMilli(pubDate.time)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(), ZoneId.of("GMT")))
                .link(link)
                .guid(guid)
                .description(html)
                .build()
    }

    @CompileDynamic
    static String renderPostHtml(HtmlPost htmlPost,
                                 String templateText,
                                 List<HtmlPost> posts) {

        StringWriter writer = new StringWriter()
        MarkupBuilder mb = new MarkupBuilder(writer)
        mb.div(class: 'content container') {
            h1 {
                a(href: '[%url]' + "/" + BLOG + "/" + INDEX) {
                    span 'Micronaut'
                    b 'Blog'
                }

            }
            div(class: 'light') {
                div(class: 'largegoldenratio align-left') {
                    div(class: 'padded') {
                        mkp.yieldUnescaped(htmlPost.html)
                    }
                }
                div(class: 'smallgoldenratio align-right') {
                    for (HtmlPost post : relatedPosts(htmlPost, posts)) {
                        mkp.yieldUnescaped(postCard(post))
                    }
                }
            }
        }
        String html = writer.toString()
        Map<String, String> metadata = htmlPost.metadata.toMap()
        html = RenderSiteTask.renderHtmlWithTemplateContent(html, metadata, templateText)
        html = RenderSiteTask.highlightMenu(html, metadata, htmlPost.path)
        metadata['body'] = metadata['body'] ? metadata['body'] : 'post'
        if (metadata['body']) {
            html = html.replace("<body>", "<body class='${metadata['body']}'>")
        }
        html
    }

    static List<HtmlPost> relatedPosts(HtmlPost htmlPost, List<HtmlPost> posts) {
        List<HtmlPost> relatedPosts = []
        for (String tag : htmlPost.tags) {
            for (HtmlPost p : posts) {
                if (p.tags.contains(tag) && p.path != htmlPost.path) {
                    List<String> paths = relatedPosts*.path
                    if (paths.contains(p.path)) {
                        continue
                    }
                    relatedPosts.add(p)
                    if (relatedPosts.size() > MAX_RELATED_POSTS) {
                        break
                    }
                }
            }
            if (relatedPosts.size() > MAX_RELATED_POSTS) {
                break
            }
        }
        if (relatedPosts.size() < MAX_RELATED_POSTS) {
            for (HtmlPost p : posts) {
                List<String> paths = relatedPosts*.path
                if (paths.contains(p.path)) {
                    continue
                }
                relatedPosts << p
                if (relatedPosts.size() > MAX_RELATED_POSTS) {
                    break
                }
            }
        }
        relatedPosts.subList(0, MAX_RELATED_POSTS).sort { a, b ->
            MMM_D_YYYY.parse(a.metadata.date).after(MMM_D_YYYY.parse(b.metadata.date)) ? -1 : 1
        }
    }

    static List<HtmlPost> processPosts(Map<String, String> globalMetadata, List<MarkdownPost> markdownPosts) {
        markdownPosts.collect { MarkdownPost mdPost ->
                Map<String, String> metadata = RenderSiteTask.processMetadata(globalMetadata + mdPost.metadata)
            PostMetadata postMetadata = new PostMetadataAdapter(metadata)
            String markdown = mdPost.content
            if (metadata.containsKey('slides')) {
                markdown = markdown + "\n\n[Slides](${metadata['slides']})\n\n"
            }
            if (metadata.containsKey('code')) {
                markdown = markdown + "\n\n[Code](${metadata['code']})\n\n"
            }
            String contentHtml = wrapTags(metadata, MarkdownUtil.htmlFromMarkdown(markdown))
            if (metadata.containsKey('video') && metadata['video'].startsWith(YOUTUBE_WATCH)) {
                String videoId = metadata['video'].substring(YOUTUBE_WATCH.length())
                contentHtml = contentHtml + "<iframe width=\"100%\" height=\"360\" src=\"https://www.youtube-nocookie.com/embed/"+videoId+"\" frameborder=\"0\"></iframe>"
            }
            Set<String> postTags = parseTags(contentHtml)
            new HtmlPost(metadata: postMetadata, html: contentHtml, path: mdPost.path, tags: postTags)

        }
    }

    static void renderPosts(Map<String, String> globalMetadata,
                            List<HtmlPost> listOfPosts,
                            File outputDir,
                            final String templateText) {
        List<String> postCards = []
        List<RssItem> rssItems = []
        Map<String, List<String>> tagPosts = [:]
        Map<String, Integer> tagsMap = [:]

        for (HtmlPost htmlPost : listOfPosts) {
            postCards << postCard(htmlPost)
            String html = renderPostHtml(htmlPost, templateText, listOfPosts)
            File pageOutput = new File(outputDir.absolutePath + "/" + htmlPost.path)
            pageOutput.createNewFile()
            pageOutput.text = html

            Set<String> postTags = parseTags(html)
            for (String postTag : postTags) {
                tagsMap[postTag] = tagsMap.containsKey(postTag) ? (1 + tagsMap[postTag]) : 1
                if (!tagPosts.containsKey(postTag)) {
                    tagPosts[postTag] = []
                }
                tagPosts[postTag] << htmlPost.path
            }
            String postLink = postLink(htmlPost)
            rssItems.add(rssItemWithPage(htmlPost.metadata.title,
                    MMM_D_YYYY.parse(htmlPost.metadata.date),
                    postLink,
                    htmlPost.path.replace(".html", ""),
                    htmlPost.html))
        }
        Set<Tag> tags = tagsMap.collect { k, v -> new Tag(title: k, ocurrence: v) } as Set<Tag>
        renderArchive(new File(outputDir.absolutePath + "/index.html"), postCards, globalMetadata, templateText, tags)
        renderRss(globalMetadata, rssItems, new File(outputDir.absolutePath + "/../" + RSS_FILE))
        renderTags(globalMetadata, outputDir, tagsMap.keySet(), listOfPosts, templateText)
    }

    static Set<String> parseTags(String html) {
        String pageHtml = html
        Set<String> tags = []
        for (; ;) {
            if (!(pageHtml.contains(HASHTAG_SPAN) && pageHtml.contains(SPAN_CLOSE))) {
                return tags
            }
            pageHtml = pageHtml.substring(pageHtml.indexOf(HASHTAG_SPAN) + HASHTAG_SPAN.length())
            String tag = pageHtml.substring(0, pageHtml.indexOf(SPAN_CLOSE))
            tags << tag
            pageHtml = pageHtml.substring(pageHtml.indexOf(SPAN_CLOSE) + SPAN_CLOSE.length())
        }
    }

    static void renderTags(Map<String, String> metadata,
                           File outputDir,
                           Set<String> tags,
                           List<HtmlPost> posts,
                           String templateText) {
        File tagFolder = new File(outputDir.absolutePath + "/${TAG}")
        tagFolder.mkdir()

        Map<String, String> resolvedMetadata = RenderSiteTask.processMetadata(metadata)

        for (String tag : tags) {
            List<String> tagCards = []
            List<HtmlPost> postsTagged = posts.findAll { it.tags.contains(tag) }
            for (HtmlPost post : postsTagged) {
                tagCards << postCard(post)
            }
            File tagFile = new File("${tagFolder.absolutePath}/${tag}.html")
            renderCards(tagFile, tagCards, resolvedMetadata, templateText, renderTagTitle(tag))
        }
    }

    static String postLink(HtmlPost post) {
        post.metadata.url + '/' + BLOG + '/' + post.path
    }

    @CompileDynamic
    private static String postCard(HtmlPost htmlPost) {
        String imageUrl = htmlPost.metadata['image'] ? (htmlPost.metadata.url + '/' + IMAGES + '/' + htmlPost.metadata['image']) : null
        StringWriter writer = new StringWriter()
        MarkupBuilder mb = new MarkupBuilder(writer)
        mb.article(class: 'blogcard', style: imageUrl ? 'background-image: url(' + imageUrl + ')' : '') {
            a(href: postLink(htmlPost)) {
                h3 {
                    mkp.yield htmlPost.metadata.date
                }
                h2 {
                    mkp.yield htmlPost.metadata.title
                }
            }
        }
        writer.toString()
    }

    @CompileDynamic
    private static String renderTagTitle(String tag) {
        StringWriter writer = new StringWriter()
        MarkupBuilder mb = new MarkupBuilder(writer)
        mb.h1 {
            span 'Tag:'
            b tag
        }
        writer.toString()
    }

    @CompileDynamic
    private static String tagsCard(Map<String, String> sitemeta, Set<Tag> tags) {
        StringWriter writer = new StringWriter()
        MarkupBuilder mb = new MarkupBuilder(writer)
        mb.article(class: 'tagcloud blogcard desktop') {
            h3 'Post by Tag'
            mkp.yieldUnescaped TagCloud.tagCloud(sitemeta['url'] + "/" + BLOG + "/" + TAG , tags, false)
        }
        return writer.toString()
    }

    @CompileDynamic
    private static String rssCard(String url) {
        StringWriter writer = new StringWriter()
        MarkupBuilder mb = new MarkupBuilder(writer)
        String imageUrl = url + "/images/feedicon.svg"
        mb.article(class: 'blogcard desktop', style: "background-image: url('${imageUrl}')".toString()) {
            h3 'Feeds'
            h2 {
                a(href: '[%url]/' + RSS_FILE, 'RSS Feed')
            }
        }
        return writer.toString()
    }
    @CompileDynamic
    private static String subscribeCard() {
        StringWriter writer = new StringWriter()
        MarkupBuilder mb = new MarkupBuilder(writer)
        mb.article(class: 'blogcard desktop') {
            mkp.yieldUnescaped('''
<!--[if lte IE 8]>
<script charset="utf-8" type="text/javascript" src="//js.hsforms.net/forms/v2-legacy.js"></script>
<![endif]-->
<script charset="utf-8" type="text/javascript" src="//js.hsforms.net/forms/v2.js"></script>
<script>
  hbspt.forms.create({
\tportalId: "4547412",
\tformId: "a675210e-7748-44bf-b603-3363d613ffb1"
});
</script>
''')
        }
        writer.toString()
    }

    @CompileDynamic
    private static void renderArchive(File f,
                                      List<String> postCards,
                                      Map<String, String> sitemeta,
                                      String templateText,
                                      Set<Tag> tags) {

        List<String> cards = []
        cards.addAll(postCards)
        cards.add(2, tagsCard(sitemeta, tags))
        cards.add(5, rssCard(sitemeta['url']))
        //cards.add(8, subscribeCard())
        Map<String, String> resolvedMetadata = RenderSiteTask.processMetadata(sitemeta)
        // String html = EventsPage.mainContent(sitemeta['url']) +
        //         cardsHtml(cards, resolvedMetadata)
        String html = cardsHtml(cards, resolvedMetadata)
        html = RenderSiteTask.renderHtmlWithTemplateContent(html, resolvedMetadata, templateText)
        html = RenderSiteTask.highlightMenu(html, resolvedMetadata, "/" + BLOG + "/" + INDEX)
        f.createNewFile()
        f.text = html
    }

    private static void renderCards(File f,
                                    List<String> cards,
                                    Map<String, String> meta,
                                    String templateText,
                                    String title = null) {
        String pageHtml = cardsHtml(cards, meta, title)
        f.createNewFile()
        f.text = RenderSiteTask.renderHtmlWithTemplateContent(pageHtml, meta, templateText)
    }

    @CompileDynamic
    static String cardsHtml(List<String> cards, Map<String, String> meta, String title = null) {
        StringWriter writer = new StringWriter()
        MarkupBuilder mb = new MarkupBuilder(writer)
        mb.div(class: 'clear content container') {
            if (title) {
                mkp.yieldUnescaped(title)
            } else {

                h1 {
                    a(href: '[%url]' + "/" + BLOG + "/" + INDEX) {
                        span 'Micronaut'
                        b 'Blog'
                    }
                }
            }

            div(class: 'light') {
                for (int i = 0; i < cards.size(); i++) {
                    if (i == 0) {
                        mkp.yieldUnescaped('<div class="threecolumns">')
                    }
                    div(class: 'column') {
                        mkp.yieldUnescaped(cards[i])
                    }

                    if ( i != 0 && ((i + 1 ) % 3 == 0)) {
                        mkp.yieldUnescaped('</div>')
                        if (i != (cards.size() - 1)) {
                            mkp.yieldUnescaped('<div class="threecolumns">')
                        }
                    }
                }
            }
        }
        writer.toString()
    }

    private static void renderRss(Map<String, String> sitemeta, List<RssItem> rssItems, File outputFile) {
        RssChannel.Builder builder = RssChannel.builder(sitemeta['title'], sitemeta['url'], sitemeta['description'])
        builder.pubDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("GMT")))
        builder.lastBuildDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("GMT")))
                .docs("http://blogs.law.harvard.edu/tech/rss")
                .generator("Micronaut RSS")
                .managingEditor("delamos@objectcomputing.com")
                .webMaster("delamos@objectcomputing.com")
        for (RssItem item : rssItems) {
            builder.item(item)
        }
        FileWriter writer = new FileWriter(outputFile)
        RssFeedRenderer rssFeedRenderer = new DefaultRssFeedRenderer()
        rssFeedRenderer.render(writer, builder.build())
        writer.close()
    }

    @Nonnull
    static String wrapTags(Map<String, String> metadata, @Nonnull String html) {
        html.split("\n")
                .collect { line ->
                    if (line.startsWith("<p>") && line.endsWith("</p>")) {
                        String lineWithoutParagraphs = line.replaceAll("<p>", "")
                                .replaceAll("</p>", "")
                        String[] words = lineWithoutParagraphs.split(" ")
                        lineWithoutParagraphs = words.collect { word ->
                            if (word.startsWith("#")) {
                                String tag = word
                                if (word.contains("<")) {
                                    tag = word.substring(0, word.indexOf("<"))
                                }
                                return "<a href=\"${metadata['url']}/${BLOG}/${TAG}/${tag.replaceAll("#", "")}.html\"><span class=\"hashtag\">${tag}</span></a>".toString()
                            } else {
                                return word
                            }
                        }.join(" ")
                        return "<p>${lineWithoutParagraphs}</p>".toString()
                    } else {
                        line
                    }
                }.join('\n')
    }

    static List<MarkdownPost> parsePosts(File posts) {
        List<MarkdownPost> listOfPosts = []
        posts.eachFile { file ->
            if (file.path.endsWith(".md") || file.path.endsWith(".markdown")) {
                ContentAndMetadata contentAndMetadata = RenderSiteTask.parseFile(file)
                listOfPosts << new MarkdownPost(filename: file.name, content: contentAndMetadata.content, metadata: contentAndMetadata.metadata)
            }
        }
        listOfPosts
    }


}
