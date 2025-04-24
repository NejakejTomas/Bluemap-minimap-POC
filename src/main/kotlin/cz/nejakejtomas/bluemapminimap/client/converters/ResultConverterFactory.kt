package cz.nejakejtomas.bluemapminimap.client.converters

import cz.nejakejtomas.bluemapminimap.runSuspendCatching
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.*
import io.ktor.client.statement.*

object ResultConverterFactory : Converter.Factory {
    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit
    ): Converter.SuspendResponseConverter<HttpResponse, Result<*>>? {
        if (typeData.typeInfo.type != Result::class) return null
        val elementType = typeData.typeArgs.first()

        return object : Converter.SuspendResponseConverter<HttpResponse, Result<*>> {
            override suspend fun convert(result: KtorfitResult): Result<Any> {
                val dataConverter = ktorfit.nextSuspendResponseConverter(
                    this@ResultConverterFactory, elementType
                )

                return when (result) {
                    is KtorfitResult.Success -> {
                        runSuspendCatching {
                            dataConverter?.convert(result) ?: result.response.body(typeData.typeArgs.first().typeInfo)
                        }
                    }

                    is KtorfitResult.Failure -> {
                        Result.failure(result.throwable)
                    }
                }
            }
        }
    }
}