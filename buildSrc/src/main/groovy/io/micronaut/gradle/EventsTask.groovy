package io.micronaut.gradle

import groovy.transform.CompileStatic
import io.micronaut.airtable.AirtableBaseApi
import io.micronaut.context.ApplicationContext
import io.micronaut.events.Event
import io.micronaut.events.EventsPage
import io.micronaut.events.MicronautAirtable
import io.micronaut.inject.qualifiers.Qualifiers
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@CompileStatic
class EventsTask extends DefaultTask {
    static final String PAGE_NAME_EVENTS = 'events.html'

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @Input
    final Property<String> url = project.objects.property(String)

    @TaskAction
    void renderEventsPage() {
        File build = output.get()
        File temp = new File(build.absolutePath + "/" + DocumentationTask.TEMP)
        temp.mkdir()

        Map<String, Object> configuration = [:]
        configuration['airtable.api-key'] = System.getenv("AIRTABLE_API_KEY")
        configuration['airtable.bases.2020.id'] =  System.getenv("AIRTABLE_BASE_ID")
        ApplicationContext applicationContext = ApplicationContext.run(configuration)
        AirtableBaseApi api = applicationContext.getBean(AirtableBaseApi, Qualifiers.byName("2020"))
        when:
        MicronautAirtable micronautAirtable = new MicronautAirtable(api)
        List<Event> events = micronautAirtable.fetchMicronautEvents()
        applicationContext.close()

        File outputFile = new File(temp.getAbsolutePath() + "/" + PAGE_NAME_EVENTS)
        outputFile.createNewFile()
        outputFile.text = "title: Events | Micronaut Framework\nbody: events\n---\n" +
                EventsPage.mainContent(events)
    }
}
