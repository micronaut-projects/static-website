package io.micronaut

import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.micronaut.Guide

import java.text.DateFormat
import java.text.SimpleDateFormat

@CompileStatic
class GuidesFetcher {

    public static final String GUIDES_JSON = 'https://raw.githubusercontent.com/micronaut-projects/micronaut-guides/gh-pages/guides.json'

    @CompileDynamic
    static List<Guide> fetchGuides() {
        URL url = new URL(GUIDES_JSON)
        def jsonArr = new JsonSlurper().parseText(url.text)
        DateFormat dateFormat = new SimpleDateFormat('dd MMM yyyy', Locale.US)
        List<SingleLanguageGuide> singleLanguageGuideList = jsonArr.collect {
            SingleLanguageGuide guide = new SingleLanguageGuide(authors: it.authors as List,
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
                Map<ProgrammingLanguage, String> githubSlugs = [:]
                githubSlugs[javaGuide.programmingLanguage] = javaGuide.githubSlug
                for (SingleLanguageGuide guide : groovyKotlinGuides) {
                    githubSlugs[guide.programmingLanguage] = guide.githubSlug
                }
                guides << new MultiLanguageGuide(authors: [javaGuide.authors, groovyKotlinGuides*.authors].flatten().unique() as List<String>,
                        category: javaGuide.category,
                        githubSlugs: githubSlugs,
                        name: javaGuide.name,
                        title: javaGuide.title,
                        subtitle: javaGuide.subtitle,
                        tags: [javaGuide.tags, groovyKotlinGuides*.tags].flatten().unique() as List<String>,
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
}
