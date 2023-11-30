package library.neetoffice.com.neetannotation.processor.kapt;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;

public class ViewModelHelp {
    static CodeBlock viewModelProvidersOf(String context_from, String key, ClassName typeName) {
        if (key == null || key.isEmpty()) {
            return CodeBlock.builder().add("$T.getInstance($N)", typeName, context_from).build();
        } else {
            return CodeBlock.builder().add("$T.getInstance($S,$N)", typeName, key, context_from).build();
        }
    }
}
