package io.micronaut.gradle

import groovy.transform.CompileStatic
import io.micronaut.download.DownloadPage
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@CompileStatic
class DownloadTask extends DefaultTask {
    static final String PAGE_NAME_DOWNLOAD = "download.html"

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @Input
    final Property<File> releases = project.objects.property(File)

    @Input
    final Property<String> url = project.objects.property(String)

    @TaskAction
    void renderDownloadPage() {
        File build = output.get()
        File temp = new File(build.absolutePath + "/" + DocumentationTask.TEMP)
        temp.mkdir()

        File outputFile = new File(temp.getAbsolutePath() + "/" + PAGE_NAME_DOWNLOAD)
        outputFile.createNewFile()
        outputFile.text = "body: download\n---\n" +
                DownloadPage.mainContent(releases.get(), url.get())
    }
}
