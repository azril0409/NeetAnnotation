package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class RxJavaClass {
    public static final ClassName AndroidSchedulers = ClassName.get("io.reactivex.android.schedulers", "AndroidSchedulers");
    public static final ClassName Consumer = ClassName.get("io.reactivex.functions", "Consumer");
    public static final ClassName Disposable = ClassName.get("io.reactivex.disposables", "Disposable");
    public static final ClassName Function = ClassName.get("io.reactivex.functions", "Function");
    public static final ClassName Observable = ClassName.get("io.reactivex", "Observable");
    public static final ClassName Observer = ClassName.get("io.reactivex", "Observer");
    public static final ClassName PublishSubject = ClassName.get("io.reactivex.subjects", "PublishSubject");
    public static final ClassName Subject = ClassName.get("io.reactivex.subjects", "Subject");
    public static final ClassName Schedulers = ClassName.get("io.reactivex.schedulers", "Schedulers");

    public static final TypeName Consumer(TypeName parameterizedTypeName) {
        return ParameterizedTypeName.get(Consumer, parameterizedTypeName);
    }

    public static final TypeName Subject(TypeName parameterizedTypeName) {
        return ParameterizedTypeName.get(Subject, parameterizedTypeName);
    }

    public static final TypeName Function(TypeName input, TypeName output) {
        return ParameterizedTypeName.get(Function, input, output);
    }
}
