package cz.nejakejtomas.bluemapminimap.client.converters

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.statement.*
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

object BufferedImageConverterFactory : Converter.Factory {
    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit
    ): Converter.SuspendResponseConverter<HttpResponse, BufferedImage>? {
        if (typeData.typeInfo.type != BufferedImage::class) return null

        return object : Converter.SuspendResponseConverter<HttpResponse, BufferedImage> {
            override suspend fun convert(result: KtorfitResult): BufferedImage {
                when (result) {
                    is KtorfitResult.Success -> {
                        val bytes = result.response.readRawBytes()
                        return ByteArrayInputStream(bytes).use {
                            ImageIO.read(it)
                        }
                    }

                    is KtorfitResult.Failure -> {
                        throw result.throwable
                    }
                }
            }
        }
    }
}