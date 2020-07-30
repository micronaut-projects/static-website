package io.micronaut.gradle

import groovy.transform.CompileStatic
import io.micronaut.questions.QuestionsPage
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@CompileStatic
class QuestionsTask extends DefaultTask {

    static final String PAGE_NAME_QUESTIONS = "faq.html"

    @Input
    final Property<File> questions = project.objects.property(File)

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @TaskAction
    void renderQuestionsPage() {
        File build = output.get()
        File temp = new File(build.absolutePath + "/" + DocumentationTask.TEMP)
        temp.mkdir()

        File output = new File(temp.getAbsolutePath() + "/" + PAGE_NAME_QUESTIONS)
        output.createNewFile()
        output.text = "body: faq\n---\n" +
                QuestionsPage.mainContent(questions.get())
    }

}
