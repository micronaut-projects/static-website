package io.micronaut.gradle;

import groovy.transform.CompileStatic
import groovy.transform.Internal
import io.micronaut.documentation.DocumentationPage;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction;

@CompileStatic
class DocumentationTask extends DefaultTask {

    static final String PAGE_NAME_DOCS = "documentation.html"

    @Input
    final Property<File> modules = project.objects.property(File)

    @Input
    final Property<File> releases = project.objects.property(File)

    @Input
    final Property<String> url = project.objects.property(String)

    @OutputDirectory
    final Property<File> pages = project.objects.property(File)

    @TaskAction
    void renderDocsPage() {
        File output = new File(pages.get().getAbsolutePath() + "/" + PAGE_NAME_DOCS)
        output.createNewFile()
        output.text = "body: docs\n---\n" +
                DocumentationPage.mainContent(releases.get(), modules.get(), url.get())
    }
}
