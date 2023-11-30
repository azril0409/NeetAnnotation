package library.neetoffice.com.neetannotation.processor.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo

class ActivityFactoryGenerator(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator
) {

    fun generate(symbol: KSAnnotated) {
        val fragment = symbol as KSClassDeclaration
        val packageName = fragment.packageName.asString()
        val factoryName = "${fragment.simpleName.asString()}_"
        logger.warn(packageName)
        logger.warn(factoryName)
        val fragmentClass = fragment.asType(emptyList())

        val fileSpec = FileSpec.builder(
            packageName = packageName, fileName = factoryName
        ).apply {
            addType(
                TypeSpec.classBuilder(factoryName).addType(
                    TypeSpec.companionObjectBuilder().build()
                ).build()
            )
        }.build()

        fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = true)
    }
}