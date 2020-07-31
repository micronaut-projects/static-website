package io.micronaut.gradle;

import groovy.transform.CompileStatic
import groovy.transform.Internal
import io.micronaut.documentation.DocumentationPage;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction;

@CompileStatic
class DocumentationTask extends DefaultTask {

    static final String PAGE_NAME_DOCS = "documentation.html"
    public static final String TEMP = "temp"

    @Input
    final Property<File> modules = project.objects.property(File)

    @Input
    final Property<File> releases = project.objects.property(File)

    @Input
    final Property<String> url = project.objects.property(String)

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @TaskAction
    void renderDocsPage() {
        File build = output.get()
        File temp = new File(build.absolutePath + "/" + TEMP)
        temp.mkdir()

        File output = new File(temp.getAbsolutePath() + "/" + PAGE_NAME_DOCS)
        output.createNewFile()
        output.text = "title: Documentation | Micronaut Framework\nbody: docs\n---\n" +
                DocumentationPage.mainContent(releases.get(), modules.get(), url.get())
    }
}
