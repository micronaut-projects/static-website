package io.micronaut.guides

import groovy.json.JsonSlurper
import groovy.time.TimeCategory
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import java.text.DateFormat
import java.text.SimpleDateFormat

@CompileStatic
class GuidesFetcher {

    public static final String GUIDES_JSON = 'https://raw.githubusercontent.com/micronaut-projects/micronaut-guides/gh-pages/guides.json'

    public static final String MICRONAUTVERSION_NAME = 'micronautVersion'

    static String versionNumber(String githubSlug) {
        Properties props = new Properties()
        props.load(new URL("https://raw.githubusercontent.com/${githubSlug}/master/complete/gradle.properties").newInputStream())
        String value = props.getProperty(MICRONAUTVERSION_NAME)
        value
    }

    @CompileDynamic
    static List<Guide> fetchGuides(boolean skipFuture = true) {
        URL url = new URL(GUIDES_JSON)
        def jsonArr = new JsonSlurper().parseText(url.text)

        DateFormat dateFormat = new SimpleDateFormat('dd MMM yyyy',
                Locale.US)
        List<SingleLanguageGuide> singleLanguageGuideList = jsonArr.collect {

            String micronautVersion = versionNumber(it.githubSlug)

            SingleLanguageGuide guide = new SingleLanguageGuide(authors: it.authors as List,
                    versionNumber: micronautVersion,
                    category: it.category,
                    githubSlug: it.githubSlug,
                    name: it.name,
                    title: it.title,
                    subtitle: it.subtitle,
                    tags: it.tags as List,
                    programmingLanguage: programmingLanguage(it.githubSlug),

            )
            if ( it.publicationDate ) {
                guide.publicationDate = dateFormat.parse(it.publicationDate as String)
            }
            guide
        }
        List<Guide> guides = []
        List<SingleLanguageGuide> javaGuideList = singleLanguageGuideList.findAll {
            it.programmingLanguage == ProgrammingLanguage.JAVA
        }
        for(SingleLanguageGuide javaGuide : javaGuideList) {
            List<SingleLanguageGuide> groovyKotlinGuides = singleLanguageGuideList.findAll {
                (it.programmingLanguage == ProgrammingLanguage.KOTLIN || it.programmingLanguage == ProgrammingLanguage.GROOVY) &&
                        ((it.githubSlug == "${javaGuide.githubSlug}-groovy") || (it.githubSlug == "${javaGuide.githubSlug}-kotlin"))
            }
            if (groovyKotlinGuides) {
                Map<ProgrammingLanguage, List<String>> tags = [:]
                tags[javaGuide.programmingLanguage] = javaGuide.tags

                Map<ProgrammingLanguage, String> githubSlugs = [:]
                githubSlugs[javaGuide.programmingLanguage] = javaGuide.githubSlug

                for (SingleLanguageGuide guide : groovyKotlinGuides) {
                    githubSlugs[guide.programmingLanguage] = guide.githubSlug
                    tags[guide.programmingLanguage] = guide.tags
                }
                String micronautVersion = versionNumber(javaGuide.githubSlug)

                guides << new MultiLanguageGuide(versionNumber: micronautVersion, authors: [javaGuide.authors, groovyKotlinGuides*.authors].flatten().unique() as List<String>,
                        category: javaGuide.category,
                        githubSlugs: githubSlugs,
                        name: javaGuide.name,
                        title: javaGuide.title,
                        subtitle: javaGuide.subtitle,
                        programmingLanguageTags: tags,
                        publicationDate: javaGuide.publicationDate)
            } else {
                guides << javaGuide
            }
        }

        for ( SingleLanguageGuide guide : singleLanguageGuideList.findAll {
            it.programmingLanguage == ProgrammingLanguage.KOTLIN || it.programmingLanguage == ProgrammingLanguage.GROOVY
        }) {
            if ( !guides.find {
                if (it instanceof SingleLanguageGuide && it.githubSlug == guide.githubSlug) {
                    return true
                }
                if (it instanceof MultiLanguageGuide && it.githubSlugs.values().contains(guide.githubSlug)) {
                    return true
                }
                return false
            }) {
                guides << guide
            }
        }
        if(skipFuture) {
            guides = guides.findAll { it.publicationDate.before(tomorrow()) }
        }
        guides.sort { Guide a, Guide b ->
            a.publicationDate <=> b.publicationDate
        }
    }

    static ProgrammingLanguage programmingLanguage(String slug) {
        if(slug.endsWith('groovy')) {
            return ProgrammingLanguage.GROOVY
        } else if(slug.endsWith('kotlin')) {
            return ProgrammingLanguage.KOTLIN
        }

        return ProgrammingLanguage.JAVA
    }


    @CompileDynamic
    static Date tomorrow() {
        Date tomorrow = new Date()
        use(TimeCategory) {
            tomorrow = tomorrow += 1.day
        }
        tomorrow
    }
}
