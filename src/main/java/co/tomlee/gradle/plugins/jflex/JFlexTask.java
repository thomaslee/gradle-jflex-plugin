package co.tomlee.gradle.plugins.jflex;

import groovy.lang.Closure;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.JavaExecSpec;

import java.io.File;

public class JFlexTask extends SourceTask {
    private FileCollection jflexClasspath;
    private File outputDirectory;

    @TaskAction
    public void generate() throws Exception {
        getProject().javaexec(new Closure(this) {
            public void doCall(JavaExecSpec javaExecSpec) {
                javaExecSpec.setMain("JFlex.Main")
                        .setClasspath(getProject().getConfigurations().getByName("jflex"))
                        .args("-d")
                        .args(getOutputDirectory())
                        .args("-q")
                        .args(getSource().getFiles());
            }
        });
    }

    @InputFiles
    public FileCollection getJflexClasspath() {
        return jflexClasspath;
    }

    public void setJflexClasspath(final FileCollection jflexClasspath) {
        this.jflexClasspath = jflexClasspath;
    }

    @OutputDirectory
    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(final File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
}
