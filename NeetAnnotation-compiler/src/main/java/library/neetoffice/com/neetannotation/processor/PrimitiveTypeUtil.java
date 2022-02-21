package library.neetoffice.com.neetannotation.processor;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

public final class PrimitiveTypeUtil {

    public static boolean isBoolean(TypeName typeName) {
        return TypeName.BOOLEAN.equals(typeName) || ClassName.get(Boolean.class).equals(typeName);
    }

    public static boolean isBoolean(TypeMirror typeMirror) {
        return isBoolean(ClassName.get(typeMirror));
    }

    public static boolean isBooleanArray(TypeName typeName) {
        return ArrayTypeName.of(TypeName.BOOLEAN).equals(typeName);
    }

    public static boolean isBooleanArray(TypeMirror typeMirror) {
        return isBooleanArray(ClassName.get(typeMirror));
    }

    public static CodeBlock booleanDefaultValue() {
        return CodeBlock.builder().add("false").build();
    }

    public static boolean isByte(TypeName typeName) {
        return TypeName.BYTE.equals(typeName) || ClassName.get(Byte.class).equals(typeName);
    }

    public static boolean isByte(TypeMirror typeMirror) {
        return isByte(ClassName.get(typeMirror));
    }

    public static boolean isByteArray(TypeName typeName) {
        return ArrayTypeName.of(TypeName.BYTE).equals(typeName);
    }

    public static boolean isByteArray(TypeMirror typeMirror) {
        return isByteArray(ClassName.get(typeMirror));
    }

    public static CodeBlock byteDefaultValue() {
        return CodeBlock.builder().add("(byte)0").build();
    }

    public static boolean isShort(TypeName typeName) {
        return TypeName.SHORT.equals(typeName) || ClassName.get(Short.class).equals(typeName);
    }

    public static boolean isShort(TypeMirror typeMirror) {
        return isShort(ClassName.get(typeMirror));
    }

    public static boolean isShortArray(TypeName typeName) {
        return ArrayTypeName.of(TypeName.SHORT).equals(typeName);
    }

    public static boolean isShortArray(TypeMirror typeMirror) {
        return isShortArray(ClassName.get(typeMirror));
    }

    public static CodeBlock shortDefaultValue() {
        return CodeBlock.builder().add("(short)0").build();
    }

    public static boolean isInt(TypeName typeName) {
        return TypeName.INT.equals(typeName) || ClassName.get(Integer.class).equals(typeName);
    }

    public static boolean isInt(TypeMirror typeMirror) {
        return isInt(ClassName.get(typeMirror));
    }

    public static boolean isIntArray(TypeName typeName) {
        return ArrayTypeName.of(TypeName.INT).equals(typeName);
    }

    public static boolean isIntArray(TypeMirror typeMirror) {
        return isIntArray(ClassName.get(typeMirror));
    }

    public static CodeBlock intDefaultValue() {
        return CodeBlock.builder().add("0").build();
    }

    public static boolean isLong(TypeName typeName) {
        return TypeName.LONG.equals(typeName) || ClassName.get(Long.class).equals(typeName);
    }

    public static boolean isLong(TypeMirror typeMirror) {
        return isLong(ClassName.get(typeMirror));
    }

    public static boolean isLongArray(TypeName typeName) {
        return ArrayTypeName.of(TypeName.LONG).equals(typeName);
    }

    public static boolean isLongArray(TypeMirror typeMirror) {
        return isLongArray(ClassName.get(typeMirror));
    }

    public static CodeBlock longDefaultValue() {
        return CodeBlock.builder().add("0L").build();
    }

    public static boolean isFloat(TypeName typeName) {
        return TypeName.FLOAT.equals(typeName) || ClassName.get(Float.class).equals(typeName);
    }

    public static boolean isFloat(TypeMirror typeMirror) {
        return isFloat(ClassName.get(typeMirror));
    }

    public static boolean isFloatArray(TypeName typeName) {
        return ArrayTypeName.of(TypeName.FLOAT).equals(typeName);
    }

    public static boolean isFloatArray(TypeMirror typeMirror) {
        return isFloatArray(ClassName.get(typeMirror));
    }

    public static CodeBlock floatDefaultValue() {
        return CodeBlock.builder().add("0f").build();
    }

    public static boolean isDouble(TypeName typeName) {
        return TypeName.DOUBLE.equals(typeName) || ClassName.get(Double.class).equals(typeName);
    }

    public static boolean isDouble(TypeMirror typeMirror) {
        return isDouble(ClassName.get(typeMirror));
    }

    public static boolean isDoubleArray(TypeName typeName) {
        return ArrayTypeName.of(TypeName.DOUBLE).equals(typeName);
    }

    public static boolean isDoubleArray(TypeMirror typeMirror) {
        return isDoubleArray(ClassName.get(typeMirror));
    }

    public static CodeBlock doubleDefaultValue() {
        return CodeBlock.builder().add("0d").build();
    }

    public static boolean isChar(TypeName typeName) {
        return TypeName.CHAR.equals(typeName) || ClassName.get(Character.class).equals(typeName);
    }

    public static boolean isChar(TypeMirror typeMirror) {
        return isChar(ClassName.get(typeMirror));
    }

    public static boolean isCharArray(TypeName typeName) {
        return ArrayTypeName.of(TypeName.CHAR).equals(typeName);
    }

    public static boolean isCharArray(TypeMirror typeMirror) {
        return isCharArray(ClassName.get(typeMirror));
    }

    public static CodeBlock charDefaultValue() {
        return CodeBlock.builder().add("'\\u0000'").build();
    }
}
