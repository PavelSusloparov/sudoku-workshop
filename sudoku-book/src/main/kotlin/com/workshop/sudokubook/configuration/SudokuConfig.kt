package com.workshop.sudokubook.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.config.RequestConfig
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import org.zalando.logbook.Logbook
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor
import org.zalando.logbook.json.CompactingJsonBodyFilter

@Configuration
class SudokuConfig(
    @Qualifier("upperCamelCaseMapper") val upperCamelCaseMapper: ObjectMapper
) {

    internal fun clientHttpRequestFactory(): ClientHttpRequestFactory {
        val config = RequestConfig.custom().build()
        val logbook = Logbook.builder()
            .bodyFilter(CompactingJsonBodyFilter(upperCamelCaseMapper))
            .build()
        val client = HttpClientBuilder
            .create()
            .setDefaultRequestConfig(config)
            .addInterceptorFirst(LogbookHttpRequestInterceptor(logbook))
            .addInterceptorLast(LogbookHttpResponseInterceptor())
            .build()
        return HttpComponentsClientHttpRequestFactory(client)
    }

    @Bean
    fun sudokuRestTemplate(): RestTemplate {
        val httpRequestFactory = clientHttpRequestFactory()
        return RestTemplate(httpRequestFactory)
    }
}
