package co.tomlee.gradle.plugins.jflex;

import jflex.Main;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

public class JFlexTask extends SourceTask {
    private FileCollection jflexClasspath;
    private File outputDirectory;

    @TaskAction
    public void generate() throws Exception {
        for (final File sourceFile : getSource().getFiles()) {
            Main.generate(new String[]{
                "-d", getOutputDirectory().getAbsolutePath(),
                "-q",
                sourceFile.getAbsolutePath()
            });
        }
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
