package co.tomlee.gradle.plugins.jflex;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.plugins.DslObject;
import org.gradle.api.internal.tasks.DefaultSourceSet;
import org.gradle.api.internal.file.collections.DirectoryFileTreeFactory;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;

import javax.inject.Inject;
import java.io.File;
import java.util.concurrent.Callable;

public final class JFlexPlugin implements Plugin<Project> {
    private final FileResolver fileResolver;
    private final DirectoryFileTreeFactory directoryFileTreeFactory;

    @Inject
    public JFlexPlugin(final FileResolver fileResolver, final DirectoryFileTreeFactory directoryFileTreeFactory) {
        this.fileResolver = fileResolver;
        this.directoryFileTreeFactory = directoryFileTreeFactory;
    }

    @Override
    public void apply(final Project project) {
        project.getPlugins().apply(JavaPlugin.class);

        configureConfigurations(project);
        configureSourceSets(project);
    }

    private void configureConfigurations(final Project project) {
        final Configuration jflexConfiguration = project.getConfigurations().create("jflex").setVisible(false);
    }

    private void configureSourceSets(final Project project) {
        project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().all(new Action<SourceSet>() {
            @Override
            public void execute(SourceSet sourceSet) {
                //
                // This logic borrowed from the antlr plugin.
                // 1. Add a new 'jflex' virtual directory mapping
                //
                final JFlexVirtualSourceDirectoryImpl jflexSourceSet =
                    new JFlexVirtualSourceDirectoryImpl(
                            ((DefaultSourceSet) sourceSet).getDisplayName(),
                            fileResolver,
                            directoryFileTreeFactory);
                new DslObject(sourceSet).getConvention().getPlugins().put("jflex", jflexSourceSet);
                final String srcDir = String.format("src/%s/jflex", sourceSet.getName());
                jflexSourceSet.getJflex().srcDir(srcDir);
                sourceSet.getAllSource().source(jflexSourceSet.getJflex());

                //
                // 2. Create a JFlexTask for this sourceSet
                //
                final String taskName = sourceSet.getTaskName("generate", "JFlexSource");
                final JFlexTask jflexTask = project.getTasks().create(taskName, JFlexTask.class);
                jflexTask.setDescription(String.format("Processes the %s JFlex files.", sourceSet.getName()));

                //
                // 3. Set up convention mapping for default sources (allows user to not have to specify)
                //
                jflexTask.setSource(jflexSourceSet.getJflex());

                //
                // 4. Set up convention mapping for handling the 'jflex' dependency configuration
                //
                jflexTask.getConventionMapping().map("jflexClasspath", new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return project.getConfigurations().getByName("jflex").copy().setTransitive(true);
                    }
                });

                //
                // 5. Set up the jflex output directory (adding to javac inputs)
                //
                final String outputDirectoryName =
                        String.format("%s/generated-src/jflex/%s", project.getBuildDir(), sourceSet.getName());
                final File outputDirectory = new File(outputDirectoryName);
                jflexTask.setOutputDirectory(outputDirectory);
                sourceSet.getJava().srcDir(outputDirectory);

                //
                // 6. Register the fact that jflex should run before compiling.
                //
                project.getTasks().getByName(sourceSet.getCompileJavaTaskName()).dependsOn(taskName);
            }
        });
    }
}
