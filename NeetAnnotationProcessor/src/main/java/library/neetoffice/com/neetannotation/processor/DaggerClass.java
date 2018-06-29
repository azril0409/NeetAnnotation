package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;

public class DaggerClass {
    public static final ClassName Component = ClassName.get("dagger", "Component");
    public static final ClassName Module = ClassName.get("dagger", "Module");
    public static final ClassName Named = ClassName.get("javax.inject", "Named");
    public static final ClassName Provides = ClassName.get("dagger", "Provides");
    public static final ClassName Qualifier = ClassName.get("javax.inject", "Qualifier");
    public static final ClassName Singleton = ClassName.get("javax.inject", "Singleton");
}
