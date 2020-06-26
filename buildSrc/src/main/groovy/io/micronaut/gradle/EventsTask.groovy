package io.micronaut.gradle

import groovy.transform.CompileStatic
import io.micronaut.download.DownloadPage
import io.micronaut.events.EventsPage
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@CompileStatic
class EventsTask extends DefaultTask {
    static final String PAGE_NAME_EVENTS = 'events.html'

    @OutputDirectory
    final Property<File> pages = project.objects.property(File)

    @Input
    final Property<String> url = project.objects.property(String)

    @TaskAction
    void renderDownloadPage() {
        File output = new File(pages.get().getAbsolutePath() + "/" + PAGE_NAME_EVENTS)
        output.createNewFile()
        ClassLoader classLoader = this.getClass().getClassLoader()
        output.text = "body: events\n---\n" +
                EventsPage.mainContent(url.get(), classLoader)
    }
}
