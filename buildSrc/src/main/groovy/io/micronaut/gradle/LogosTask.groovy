package io.micronaut.gradle

import groovy.transform.CompileStatic
import io.micronaut.gallery.MicronautLogo
import io.micronaut.gallery.MicronautLogosPage
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.Yaml

@CompileStatic
class LogosTask extends DefaultTask {

    static final String PAGE_NAME_MN_LOGOS = "micronaut-logos.html"

    @Input
    final Property<File> logos = project.objects.property(File)

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @TaskAction
    void renderLogosPage() {
        File build = output.get()
        File temp = new File("${build.absolutePath}/${DocumentationTask.TEMP}")
        temp.mkdir()

        File logosYaml = logos.get()
        List<MicronautLogo> logos = parseLogos(logosYaml)

        String absolutePath = "${logosYaml.getParentFile().absolutePath}/../assets"
        verifyLogosExist(absolutePath, logos)
        File output = new File("${temp.absolutePath}/${PAGE_NAME_MN_LOGOS}")
        output.createNewFile()
        output.text = "title: Logos | Micronaut Framework\n---\n" +
                MicronautLogosPage.mainContent(logos)
    }

    static List<MicronautLogo> parseLogos(File logosYaml) {
        new Yaml().load(logosYaml.newDataInputStream())['logos'].collect {
            new MicronautLogo(label: it['label'] as String,
                    thumb: it['thumb'] as String,
                    png: it['png'] as String,
                    svg: it['svg'] as String,
                    background: it['background'] as String)
        }
    }

    static void verifyLogosExist(String absolutePath, List<MicronautLogo> logos) {
        logos.each { logo ->
            [logo.svg, logo.png, logo.thumb].findAll { it }.collect {
                "${absolutePath}${it}".toString()
            }.each { pathName ->
                if (!new File(pathName).exists()) {
                    throw new GradleException("${pathName} does not exist")
                }
            }
        }
    }

}
