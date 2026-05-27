package org.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Mojo(name = "generate_garbage", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GarbageCreatorMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "generate_garbage.classCount", defaultValue = "3")
    private int classCount;

    @Parameter(property = "generate_garbage.packageName", defaultValue = "org.garbage.generated")
    private String packageName;

    @Parameter(property = "generate_garbage.classPrefix", defaultValue = "FakeClass")
    private String classPrefix;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("start to generate classes");
        File outputDirectory = new File(project.getBuild().getDirectory(), "generated-sources/garbage");
        File packageDirectory = new File(outputDirectory, packageName.replace('.', '/'));

        if (!packageDirectory.exists() && !packageDirectory.mkdirs()) {
            throw new MojoExecutionException("couldn't create dir:  " + packageDirectory.getAbsolutePath());
        }

        for (int i = 1; i <= classCount; i++) {
            String className = classPrefix + i;
            File javaFile = new File(packageDirectory, className + ".java");

            String classContent = "package " + packageName + ";\n\n" +
                    "public class " + className + " {\n" +
                    "    public void doNothing() {\n" +
                    "    }\n" +
                    "}\n";

            try (FileWriter writer = new FileWriter(javaFile)) {
                writer.write(classContent);
            } catch (IOException e) {
                throw new MojoExecutionException("Error IO: " + javaFile.getName(), e);
            }
        }

        getLog().info("classes were generated in dir: " + packageDirectory.getAbsolutePath());

        this.project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
    }
}
