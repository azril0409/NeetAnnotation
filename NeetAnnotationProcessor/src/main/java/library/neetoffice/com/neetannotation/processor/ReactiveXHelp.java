package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.concurrent.TimeUnit;

public class ReactiveXHelp {


    public static CodeBlock delay(long dalayTimeMillis) {
        return CodeBlock.builder().add(".delay(").add(String.valueOf(dalayTimeMillis)).add(", $T.MILLISECONDS)",TimeUnit.class).build();
    }

    public static CodeBlock observeOnMain() {
        return CodeBlock.builder().add(".observeOn($T.mainThread())", RxJavaClass.AndroidSchedulers).build();
    }

    public static CodeBlock observeOnThread() {
        return CodeBlock.builder().add(".observeOn($T.newThread())", RxJavaClass.Schedulers).build();
    }

    public static CodeBlock subscribeOnMain() {
        return CodeBlock.builder().add(".subscribeOn($T.mainThread())", RxJavaClass.AndroidSchedulers).build();
    }

    public static CodeBlock subscribeOnThread() {
        return CodeBlock.builder().add(".subscribeOn($T.newThread())", RxJavaClass.Schedulers).build();
    }

    public static CodeBlock subscribeConsumer(TypeName parameterizedType, String parameterized, CodeBlock accept) {
        return CodeBlock.builder()
                .beginControlFlow(".subscribe(new $T()", RxJavaClass.Consumer(parameterizedType))
                .beginControlFlow("@$T\npublic void accept($T $N) throws $T", Override.class, parameterizedType, parameterized,Exception.class)
                .add(accept)
                .endControlFlow()
                .endControlFlow(")")
                .build();
    }

    public static CodeBlock newConsumer(TypeName parameterizedType, String parameterized, CodeBlock accept){
        return CodeBlock.builder()
                .beginControlFlow("new $T()",RxJavaClass.Consumer(parameterizedType))
                .beginControlFlow("@$T\npublic void accept($T $N) throws $T", Override.class, parameterizedType, parameterized,Exception.class)
                .add(accept)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    public static CodeBlock newAction(CodeBlock accept){
        return CodeBlock.builder()
                .beginControlFlow("new $T()",RxJavaClass.Action)
                .beginControlFlow("@$T\npublic void run() throws $T", Override.class, Exception.class)
                .add(accept)
                .endControlFlow()
                .endControlFlow()
                .build();
    }



    public static CodeBlock just(String parameterName) {
        return CodeBlock.builder().add("$T.just($N)", RxJavaClass.Observable, parameterName).build();
    }

    public static CodeBlock map(TypeName parameterType, TypeName entityType, String entityName, CodeBlock apply) {
        return CodeBlock.builder()
                .beginControlFlow(".map(new $T()", RxJavaClass.Function(parameterType, entityType))
                .beginControlFlow("@$T\npublic $T apply($T $N) throws $T", Override.class, entityType, parameterType, entityName,Exception.class)
                .add(apply)
                .endControlFlow()
                .add("})")
                .build();
    }

    public static CodeBlock subscribe(TypeName parameterType, String observerName) {
        return CodeBlock.builder()
                .add(subscribeConsumer(parameterType, "s", CodeBlock.builder()
                        .addStatement("$N.onNext(s)", observerName)
                        .build()))
                .build();
    }
}
