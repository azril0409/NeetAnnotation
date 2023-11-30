package library.neetoffice.com.neetannotation.processor.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import library.neetoffice.com.neetannotation.NActivity
import library.neetoffice.com.neetannotation.processor.AndroidClass

fun KSClassDeclaration.isSubclassOf(superClassName: String): Boolean {
    val superClasses = superTypes.toMutableList() //2
    while (superClasses.isNotEmpty()) { //3
        val current = superClasses.first()
        val declaration = current.resolve().declaration //4
        when {
            declaration is KSClassDeclaration
                    && declaration.qualifiedName?.asString() == superClassName -> { //5
                return true
            }

            declaration is KSClassDeclaration -> {
                superClasses.removeAt(0) //6
                superClasses.addAll(0, declaration.superTypes.toList())
            }

            else -> {
                superClasses.removeAt(0) //7
            }
        }
    }
    return false //8
}

fun KSClassDeclaration.geNActivity(): KSAnnotation {
    val annotationKClass = NActivity::class
    return annotations.filter {
        it.annotationType
            .resolve()
            .declaration.qualifiedName?.asString() == annotationKClass.qualifiedName
    }.first()
}

fun KSClassDeclaration.isActivity(): Boolean {
    return isSubclassOf("${AndroidClass.Activity}") ||
            isSubclassOf("${AndroidClass.ComponentActivity}")
}