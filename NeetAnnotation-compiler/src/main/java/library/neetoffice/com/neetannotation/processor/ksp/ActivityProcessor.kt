package library.neetoffice.com.neetannotation.processor.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.validate
import library.neetoffice.com.neetannotation.NActivity


class ActivityProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) =
        ActivityProcessor(environment.logger, environment.codeGenerator)
}
class ActivityProcessor(private val logger: KSPLogger, codeGenerator: CodeGenerator):
    SymbolProcessor {
    private val activityValidator = NActivityValidator()
    private val activityFactoryGenerator = ActivityFactoryGenerator(logger, codeGenerator)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("$this on process.")
        logger.warn(NActivity::class.qualifiedName!!)
        val resolved = resolver.getSymbolsWithAnnotation(NActivity::class.qualifiedName!!).toList()
        logger.warn("$resolved")
        val validatedSymbols = resolved.filter { it.validate() }.toList()
        validatedSymbols.filter {
            activityValidator.isValid(it)
        }.forEach {
            activityFactoryGenerator.generate(it)
        }
        logger.warn("$validatedSymbols")
        return resolved - validatedSymbols.toSet()
    }

    override fun finish() {
        super.finish()
        logger.warn("$this on finish.")
    }

    override fun onError() {
        super.onError()
        logger.warn("$this on error.")
    }
}