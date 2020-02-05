package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;

public class ViewModelHelp {
    static CodeBlock viewModelProvidersOf(String context_from, String key, ClassName typeName) {
        if (key == null || key.isEmpty()) {
            return CodeBlock.builder().add("new $T($N).get($T.class)", AndroidClass.ViewModelProvider, context_from, typeName).build();
        } else {
            return CodeBlock.builder().add("new $T($N).get($S,$T.class)", AndroidClass.ViewModelProvider, context_from, key, typeName).build();
        }
    }
}
