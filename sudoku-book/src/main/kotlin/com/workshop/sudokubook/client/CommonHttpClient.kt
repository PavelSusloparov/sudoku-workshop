package com.workshop.sudokubook.client

import java.util.logging.Logger
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import kotlin.reflect.KClass

@Component
class CommonHttpClient(
    val hbRestTemplate: RestTemplate,
    @Qualifier("upperCamelCaseMapper") val upperCamelCaseMapper: ObjectMapper
) {

    companion object {
        val logger: Logger = Logger.getLogger(this::class.java.simpleName)
    }

    fun <T : Any> post(requestUrl: String, requestBody: Any, clazz: KClass<T>): T {
        return sendRequest(HttpMethod.POST, requestUrl, clazz, requestBody, HttpHeaders())
    }

    fun <T : Any> put(requestUrl: String, requestBody: Any, clazz: KClass<T>): T {
        return sendRequest(HttpMethod.PUT, requestUrl, clazz, requestBody, HttpHeaders())
    }

    fun <T : Any> delete(requestUrl: String, requestBody: Any?, clazz: KClass<T>): T {
        return sendRequest(HttpMethod.DELETE, requestUrl, clazz, requestBody, HttpHeaders())
    }

    fun <T : Any> get(requestUrl: String, clazz: KClass<T>): T {
        return sendRequest(HttpMethod.GET, requestUrl, clazz, null, HttpHeaders())
    }

    fun <T : Any> sendRequest(
        method: HttpMethod,
        requestUrl: String,
        responseClass: KClass<T>,
        requestBody: Any?,
        headers: HttpHeaders
    ): T {

        headers.set("Content-Type", "application/json")

        val request = HttpEntity(requestBody, headers)
        return try { // pass the value of the try up...

            val response = hbRestTemplate.exchange(requestUrl, method, request, JsonNode::class.java)
            if (!response.statusCode.is2xxSuccessful) {
                throw Exception("Http Error while making a request $requestUrl: ${response.statusCodeValue}")
            }
            upperCamelCaseMapper.convertValue(response.body!!, responseClass.java) // pass the response up
        } catch (e: HttpClientErrorException) {
            logger.severe { "sendRequest(): HttpClientErrorException " + e.responseBodyAsString }
            throw Exception("sendRequest(): HttpClientErrorException " + e.responseBodyAsString)
        }
    }
}
