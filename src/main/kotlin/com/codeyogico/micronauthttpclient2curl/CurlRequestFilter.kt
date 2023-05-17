package com.codeyogico.micronauthttpclient2curl

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpHeaders
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.ClientFilterChain
import io.micronaut.http.filter.HttpClientFilter
import org.reactivestreams.Publisher


@Filter("/api/applications/**")
class CurlRequestFilter(private val objectMapper: ObjectMapper):HttpClientFilter {
    override fun doFilter(request: MutableHttpRequest<*>, chain: ClientFilterChain): Publisher<out HttpResponse<*>> {

        println("==============================================================")
        val command = buildCurlCommand(request)
        println(command)
        println("==============================================================")
        return chain.proceed(request)
    }



    private fun buildCurlCommand(request: MutableHttpRequest<*>) =

        listOf(listOf("curl"),generateMethod(request.method),generateHeaders(request.headers),getBodyAsString(request),generateUrl(request)).flatten()
            .joinToString(" ")


    private fun getBodyAsString(request: MutableHttpRequest<*>):List<String>{
        return if(request.body.isEmpty)
            emptyList()
        else
            listOf(FORMAT_BODY.format( objectMapper.writeValueAsString(request.body)))
    }

    private fun generateUrl(request: MutableHttpRequest<*>): List<String> = listOf(FORMAT_URL.format(request.uri.toString()))

    private fun generateHeaders(headers: MutableHttpHeaders): List<String> {
        return headers.map {
            FORMAT_HEADER.format(it.key, it.value.first())
        }
    }

    private fun generateMethod(method: HttpMethod): List<String> {
        return listOf(FORMAT_METHOD.format(method))
    }


    private companion object {
        const val FORMAT_HEADER = "-H \"%1\$s:%2\$s\""
        const val FORMAT_METHOD = "-X %1\$s"
        const val FORMAT_BODY = "-d '%1\$s'"
        const val FORMAT_URL = "\"%1\$s\""
        const val CONTENT_TYPE = "Content-Type"
    }

}