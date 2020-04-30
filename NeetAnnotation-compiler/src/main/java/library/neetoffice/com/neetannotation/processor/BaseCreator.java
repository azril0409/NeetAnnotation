package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

public abstract class BaseCreator {
    final AbstractProcessor processor;
    final ProcessingEnvironment processingEnv;

    public BaseCreator(AbstractProcessor processor, ProcessingEnvironment processingEnv) {
        this.processor = processor;
        this.processingEnv = processingEnv;
    }

    abstract void process(TypeElement element, RoundEnvironment roundEnv);

    void printMessage(Diagnostic.Kind kind, CharSequence message) {
        processingEnv.getMessager().printMessage(kind, message);
    }

    String toUpperCaseFirst(String name) {
        return name.toUpperCase().charAt(0) + name.substring(1);
    }

    String toUpperCaseFirst(Name name) {
        return toUpperCaseFirst(name.toString());
    }

    String toModelCase(String name) {
        return name.toLowerCase().charAt(0) + name.substring(1);
    }

    String toModelCase(Name name) {
        return toModelCase(name.toString());
    }

    String getPackageName(Element element) {
        return processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
    }

    void writeTo(String packageName, TypeSpec typeSpec) {
        try {
            final JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
            javaFile.writeTo(processingEnv.getFiler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static TypeName getClassName(TypeMirror typeMirror) {
        final TypeName typeName = ClassName.get(typeMirror);
        if (typeName.isPrimitive()) {
            if ("char".equals(typeMirror.toString())) {
                return ClassName.get(Character.class);
            } else if ("byte".equals(typeMirror.toString())) {
                return ClassName.get(Byte.class);
            } else if ("short".equals(typeMirror.toString())) {
                return ClassName.get(Short.class);
            } else if ("int".equals(typeMirror.toString())) {
                return ClassName.get(Integer.class);
            } else if ("long".equals(typeMirror.toString())) {
                return ClassName.get(Long.class);
            } else if ("float".equals(typeMirror.toString())) {
                return ClassName.get(Float.class);
            } else if ("double".equals(typeMirror.toString())) {
                return ClassName.get(Double.class);
            } else if ("boolean".equals(typeMirror.toString())) {
                return ClassName.get(Boolean.class);
            }
        }
        return typeName;
    }


    public static CodeBlock addNullCode(TypeMirror typeMirror) {
        return addNullCode(getClassName(typeMirror));
    }

    public static CodeBlock addNullCode(TypeName typeName) {
        if (typeName.isPrimitive()) {
            if ("char".equals(typeName.toString())) {
                return CodeBlock.builder().add("(char)0").build();
            } else if ("byte".equals(typeName.toString())) {
                return CodeBlock.builder().add("(byte)0").build();
            } else if ("short".equals(typeName.toString())) {
                return CodeBlock.builder().add("(short)0").build();
            } else if ("int".equals(typeName.toString())) {
                return CodeBlock.builder().add("(int)0").build();
            } else if ("long".equals(typeName.toString())) {
                return CodeBlock.builder().add("(long)0").build();
            } else if ("float".equals(typeName.toString())) {
                return CodeBlock.builder().add("(float)0").build();
            } else if ("double".equals(typeName.toString())) {
                return CodeBlock.builder().add("(double)0").build();
            } else if ("boolean".equals(typeName.toString())) {
                return CodeBlock.builder().add("false").build();
            }
        } else if ("java.lang.Character".equals(typeName.toString())) {
            return CodeBlock.builder().add("(char)0").build();
        } else if ("java.lang.Byte".equals(typeName.toString())) {
            return CodeBlock.builder().add("(byte)0").build();
        } else if ("java.lang.Short".equals(typeName.toString())) {
            return CodeBlock.builder().add("(short)0").build();
        } else if ("java.lang.Integer".equals(typeName.toString())) {
            return CodeBlock.builder().add("(int)0").build();
        } else if ("java.lang.Long".equals(typeName.toString())) {
            return CodeBlock.builder().add("(long)0").build();
        } else if ("java.lang.Float".equals(typeName.toString())) {
            return CodeBlock.builder().add("(float)0").build();
        } else if ("java.lang.Double".equals(typeName.toString())) {
            return CodeBlock.builder().add("(double)0").build();
        } else if ("java.lang.Boolean".equals(typeName.toString())) {
            return CodeBlock.builder().add("false").build();
        }
        return CodeBlock.builder().add("null").build();
    }

    boolean isSubActivity(TypeElement element) {
        return isInstanceOf(element, AndroidClass.Activity);
    }

    boolean isSubFragmentActivity(TypeElement element) {
        return isInstanceOf(element, AndroidClass.FragmentActivity);
    }

    boolean isSubFragment(TypeElement element) {
        return isInstanceOf(element, AndroidClass.Fragment);
    }

    boolean isSubAndroidViewModel(TypeElement element) {
        return isInstanceOf(element, AndroidClass.AndroidViewModel);
    }

    boolean isSubService(TypeElement element) {
        return isInstanceOf(element, AndroidClass.Service);
    }

    private boolean isInstanceOf(TypeElement element, ClassName subClass) {
        return isInstanceOf(element.asType(), subClass.toString());
    }

    public boolean isInstanceOf(TypeMirror elementClass, ClassName subClass) {
        return isInstanceOf(elementClass, subClass.toString());
    }

    public boolean isInstanceOf(TypeMirror elementClass, String className) {
        return processingEnv.getTypeUtils().isAssignable(processingEnv.getTypeUtils().erasure(elementClass), processingEnv.getElementUtils().getTypeElement(className).asType());
    }

    public static AnnotationMirror findAnnotationMirror(Element element, TypeName AnnotationClass) {
        AnnotationMirror annotationMirror = null;
        final List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror annotation : annotationMirrors) {
            if (AnnotationClass.toString().equals(annotation.getAnnotationType().toString())) {
                annotationMirror = annotation;
            }
        }
        return annotationMirror;
    }

    public static AnnotationMirror findAnnotationMirror(Element element, Class<?> annotationClass) {
        return findAnnotationMirror(element, ClassName.get(annotationClass));
    }

    Object findAnnotationValue(Element element, Class<?> annotationClass, String executableName) {
        return findAnnotationValue(findAnnotationMirror(element, annotationClass), executableName);
    }

    Object findAnnotationValue(Element element, TypeName annotationClass, String executableName) {
        return findAnnotationValue(findAnnotationMirror(element, annotationClass), executableName);
    }

    Object findAnnotationValue(AnnotationMirror annotationMirror, String executableName) {
        Object value = null;
        if (annotationMirror != null) {
            final Map<? extends ExecutableElement, ? extends AnnotationValue> map = annotationMirror.getElementValues();
            for (ExecutableElement executable : map.keySet()) {
                final AnnotationValue annotationValue = map.get(executable);
                if (executableName.equals(executable.getSimpleName().toString())) {
                    value = annotationValue.getValue();
                }
            }
        }
        return value;
    }

    List<TypeElement> findSuperElements(TypeElement typeElement, RoundEnvironment roundEnv, Class<? extends Annotation> annotation) {
        final List<TypeElement> superElement = new ArrayList<>();
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
        TypeMirror superClass = typeElement.getSuperclass();
        boolean isSuperElement;
        do {
            isSuperElement = false;
            for (Element element : elements) {
                if (element.asType().equals(superClass)) {
                    superClass = ((TypeElement) element).getSuperclass();
                    superElement.add(0, (TypeElement) element);
                    isSuperElement = true;
                }
            }
        } while (isSuperElement);
        return superElement;
    }
}
