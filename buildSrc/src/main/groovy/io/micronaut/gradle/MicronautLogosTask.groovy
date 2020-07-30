package io.micronaut.gradle

import groovy.transform.CompileStatic
import io.micronaut.gallery.MicronautLogosPage
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@CompileStatic
class MicronautLogosTask extends DefaultTask {

    static final String PAGE_NAME_MN_LOGOS = "micronaut-logos.html"

    @Input
    final Property<File> logos = project.objects.property(File)

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @TaskAction
    void renderLogosPage() {
        File build = output.get()
        File temp = new File("${build.absolutePath}/$DocumentationTask.TEMP")
        temp.mkdir()

        File output = new File("${temp.absolutePath}/$PAGE_NAME_MN_LOGOS")
        output.createNewFile()
        output.text = "body: logos\n---\n" +
                MicronautLogosPage.mainContent(logos.get())
    }

}
