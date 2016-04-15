package co.tomlee.gradle.plugins.jflex;

import groovy.lang.Closure;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.file.collections.DirectoryFileTreeFactory;
import org.gradle.util.ConfigureUtil;

public class JFlexVirtualSourceDirectoryImpl implements JFlexVirtualSourceDirectory {
    private final SourceDirectorySet jflex;

    public JFlexVirtualSourceDirectoryImpl(
            final String parentDisplayName,
            final FileResolver fileResolver,
            final DirectoryFileTreeFactory directoryFileTreeFactory) {
        final String displayName = String.format("%s JFle source", parentDisplayName);
        this.jflex = new DefaultSourceDirectorySet(displayName, fileResolver, directoryFileTreeFactory);
        this.jflex.getFilter().include("**/*.l", "**/*.jflex");
    }

    @Override
    public SourceDirectorySet getJflex() {
        return jflex;
    }

    @Override
    public JFlexVirtualSourceDirectory jflex(Closure closure) {
        ConfigureUtil.configure(closure, getJflex());
        return this;
    }
}
