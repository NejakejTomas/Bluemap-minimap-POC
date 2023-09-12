package cz.nejakejtomas.bluemapminimap

import inet.ipaddr.IPAddressString
import inet.ipaddr.IPAddressStringParameters
import io.ktor.http.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL

private fun ensureHttp(url: String): String {
    return if (url.startsWith("http://") || url.startsWith("https://")) url
    else "http://$url"
}

fun urlFromMinecraft(url: String): Url? {
    return try {
        Url(ensureHttp(url))
    } catch (_: URLParserException) {
        null
    }
}


private val ipParams = IPAddressStringParameters.Builder().apply {
    allowEmpty(false)
    setEmptyAsLoopback(false)
    allowPrefix(false)
    allowMask(false)
    allowPrefixOnly(false)
    allowIPv4(false)
    allowIPv6(false)
    allowSingleSegment(false)
    iPv4AddressParametersBuilder.apply {
        allow_inet_aton_leading_zeros(false)
        allow_inet_aton_joined_segments(false)
        allow_inet_aton(false)
        allowWildcardedSeparator(false)
        allowWildcardedSeparator(false)

    }
    iPv6AddressParametersBuilder.apply {
        allowLeadingZeros(false)
        allowZone(false)
        allowMixed(false)
        allow_mixed_inet_aton(false)
    }
}

fun urlFromUser(url: String): Url? {
    return try {
        val uri = URL(ensureHttp(url)).toURI()
        if (uri.host == null) return null

        // IP test
        val ip = if (uri.host.startsWith('[') && uri.host.endsWith(']')) {
            // IPv6
            ipParams.allowIPv6(true)
            val ip = IPAddressString(uri.host.trimStart('[').trimEnd(']'), ipParams.toParams())
            ipParams.allowIPv6(false)

            ip
        } else {
            // IPv4
            ipParams.allowIPv4(true)
            val ip = IPAddressString(uri.host.trimStart('[').trimEnd(']'), ipParams.toParams())
            ipParams.allowIPv4(false)

            ip
        }

        if (!ip.isValid) {
            // Uri is not an IP
            val split = uri.host.split('.')
            if (split.isEmpty()) return null
            if (split.count() == 1 && split[0] != "localhost") return null
            if (split.any { it.isEmpty() }) return null
        }

        URLBuilder(uri.toString()).apply {
            if (host.isEmpty()) return null
            fragment = ""
        }.build()
    } catch (e: URLParserException) {
        null
    } catch (e: URISyntaxException) {
        null
    } catch (e: MalformedURLException) {
        null
    }
}

fun <T> Deferred<T>.valueOrNull(): T? {
    if (!isCompleted) return null

    return try {
        runBlocking { await() }
    } catch (_: Throwable) {
        null
    }
}
