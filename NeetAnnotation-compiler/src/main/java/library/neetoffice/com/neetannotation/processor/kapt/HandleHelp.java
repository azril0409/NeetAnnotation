package library.neetoffice.com.neetannotation.processor.kapt;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;

import java.util.ArrayList;
import java.util.Iterator;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import library.neetoffice.com.neetannotation.ThreadOn;
import library.neetoffice.com.neetannotation.processor.RxJavaClass;

public class HandleHelp {
    static class HandleElement{
        final ExecutableElement element;
        final CodeBlock codeBlock;

        HandleElement(ExecutableElement element, CodeBlock codeBlock) {
            this.element = element;
            this.codeBlock = codeBlock;
        }
    }
    private final BaseCreator creator;

    public HandleHelp(BaseCreator creator) {
        this.creator = creator;
    }

    Builder builder(String className) {
        return new Builder(creator,className);
    }


    static class Builder {
        private final BaseCreator creator;
        private final String className;
        private final ArrayList<HandleElement> handleElements = new ArrayList<>();

        public Builder(BaseCreator creator, String className) {
            this.creator = creator;
            this.className = className;
        }


        public void parseElement(Element element) {
            if(!(element instanceof ExecutableElement)){
                return;
            }
            if (element.getAnnotation(ThreadOn.class) != null) {
                final ExecutableElement e = (ExecutableElement) element;
                final ThreadOn aThreadOn = e.getAnnotation(ThreadOn.class);
                final CodeBlock.Builder cb = CodeBlock.builder();
                cb.add("$T.empty()", RxJavaClass.Observable);
                if(aThreadOn.value() == ThreadOn.Mode.UIThread){
                    if (aThreadOn.delayMillis() > 0) {
                        cb.add(ReactiveXHelp.delay(aThreadOn.delayMillis()));
                    }
                    cb.add(ReactiveXHelp.observeOnMain());
                }else if(aThreadOn.value() == ThreadOn.Mode.Background){
                    if (aThreadOn.delayMillis() > 0) {
                        cb.add(ReactiveXHelp.delay(aThreadOn.delayMillis()));
                    }
                    cb.add(ReactiveXHelp.observeOnThread());
                }
                cb.add(".subscribe($T.emptyConsumer()",RxJavaClass.Functions);
                cb.add(",$T.emptyConsumer()",RxJavaClass.Functions);
                cb.beginControlFlow(",new $T()",RxJavaClass.Action);
                cb.beginControlFlow("@$T public void run() throws $T",Override.class,Exception.class);
                cb.add("$N.super.$N(",className,e.getSimpleName());
                final Iterator<? extends VariableElement> iterator = e.getParameters().iterator();
                while (iterator.hasNext()){
                    final VariableElement ve = iterator.next();
                    cb.add("$N",ve.getSimpleName());
                    if(iterator.hasNext()){
                        cb.add(",");
                    }
                }
                cb.addStatement(")");
                cb.endControlFlow();
                cb.endControlFlow();
                cb.addStatement(")");
                handleElements.add(new HandleElement(e,cb.build()));
            }
        }

        public ArrayList<MethodSpec> createMotheds(){
            final ArrayList<MethodSpec> list = new ArrayList<>();
            for(HandleElement handleElement:handleElements){
                MethodSpec.Builder build =  MethodSpec.methodBuilder(handleElement.element.getSimpleName().toString());
                build.addModifiers(handleElement.element.getModifiers());
                for(VariableElement element: handleElement.element.getParameters()){
                    build.addParameter(creator.getClassName(element.asType()),element.getSimpleName().toString(),Modifier.FINAL);
                }
                build.addCode(handleElement.codeBlock);
                list.add(build.build());
            }

            return list;
        }
    }
}
