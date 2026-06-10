package annotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes("annotations.GenerateJson")
@SupportedSourceVersion(SourceVersion.RELEASE_24)
public class JsonProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(GenerateJson.class)) {
            GenerateJson annotation = element.getAnnotation(GenerateJson.class);
            String newClassName = annotation != null ? annotation.className() : null;
            String packageName = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();

            try {
                JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(packageName + "." + newClassName);

                try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                    out.println("package " + packageName + ";");
                    out.println("");
                    out.println("public class " + newClassName + " {");

                    StringBuilder constructorParams = new StringBuilder();
                    StringBuilder constructorBody = new StringBuilder();

                    for (Element enclosed : element.getEnclosedElements()) {
                        if (enclosed.getKind() == ElementKind.FIELD) {
                            String fieldType = enclosed.asType().toString();
                            String fieldName = enclosed.getSimpleName().toString();
                            String snakeCaseKey = fieldName.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();

                            out.println("@annotations.JsonKey(\"" + snakeCaseKey + "\")");
                            out.println("public " + fieldType + " " + fieldName + ";");
                            out.println("");

                            if (!constructorParams.isEmpty()) {
                                constructorParams.append(", ");
                            }
                            constructorParams.append(fieldType).append(" ").append(fieldName);

                            constructorBody.append("this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
                        }
                    }

                    out.println("public " + newClassName + "() {}");
                    out.println("");

                    out.println("public " + newClassName + "(" + constructorParams.toString() + ") {");
                    out.print(constructorBody.toString());
                    out.println("}");

                    out.println("}");
                }
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }
        return true;
    }
}