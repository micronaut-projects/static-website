package io.micronaut.gradle

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.nio.file.Paths

@CompileStatic
class BuildGuidesRedirectTask extends DefaultTask {

    @OutputDirectory
    final Property<File> output = project.objects.property(File)
    
    @TaskAction
    void renderRedirect() {
        File o = new File(output.get().absolutePath + "/" + RenderSiteTask.DIST)
        o.mkdir()
        File index = Paths.get(o.absolutePath, "index.html").toFile();
        index.text = project.file("templates/guides-template.html").text
        index.createNewFile()
    }
}
